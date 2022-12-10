package org.tinger.jdbc.mysql;

import org.tinger.common.utils.ClassUtils;
import org.tinger.common.utils.CollectionUtils;
import org.tinger.common.utils.StringUtils;
import org.tinger.jdbc.core.JdbcDriver;
import org.tinger.jdbc.handler.JdbcHandler;
import org.tinger.jdbc.metadata.JdbcMetadata;
import org.tinger.jdbc.metadata.JdbcMetadataFactory;
import org.tinger.jdbc.metadata.JdbcProperty;
import org.tinger.jdbc.queryable.Criteria;
import org.tinger.jdbc.queryable.Update;

import javax.sql.DataSource;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by tinger on 2022-10-17
 */
public abstract class AbstractMysqlRepository<T, K> {

    private static final String CREATE_SQL = "INSERT INTO `[]`.`[]` ([]) VALUES ([])";
    private static final String UPDATE_SQL = "UPDATE `[]`.`[]` SET [] WHERE []";
    private static final String SELECT_ALL_SQL = "SELECT [] FROM `[]`.`[]`";
    private static final String DELETE_SQL = "DELETE FROM `[]`.`[]` WHERE []";
    private static final String WHERE = "[] WHERE []";
    protected final JdbcMetadata<T, K> metadata;
    protected final JdbcProperty primaryKey;
    private final Logger logger = Logger.getLogger("MYSQL");

    @SuppressWarnings("unchecked")
    protected AbstractMysqlRepository() {
        List<Class<?>> classes = ClassUtils.getGenericSuperclass(this);
        this.metadata = JdbcMetadataFactory.getInstance().build(JdbcDriver.MYSQL, (Class<T>)(classes.get(0)), this.getClass());
        this.primaryKey = metadata.getPrimaryProperty();
    }

    protected K getId(T document) {
        Object value = primaryKey.getValue(document);
        if (value == null) {
            return null;
        } else {
            return (K) value;
        }
    }

    private void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ignore) {

            }
        }
    }

    private void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException ignore) {

            }
        }
    }

    private void close(PreparedStatement statement) {
        if (statement != null) {
            try {
                statement.clearParameters();
                statement.close();
            } catch (SQLException ignore) {

            }
        }
    }

    private List<T> read(ResultSet resultSet) throws Exception {
        List<JdbcProperty> properties = metadata.getProperties();

        LinkedList<T> list = new LinkedList<>();
        while (resultSet.next()) {
            T item = metadata.getConstructor().newInstance();
            list.add(item);
            int index = 0;
            for (JdbcProperty property : properties) {
                Object value = property.getHandler().getResult(resultSet, index + 1);
                if (value != null) {
                    property.setValue(item, value);
                }
                index += 1;
            }
        }
        return list;
    }

    protected int update(DataSource source, String command) {
        logger.info(command);
        Connection connection = null;
        Statement statement = null;
        try {
            connection = source.getConnection();
            statement = connection.createStatement();
            return statement.executeUpdate(command);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(statement);
            close(connection);
        }
    }

    protected int update(DataSource source, String command, List<JdbcHandler<?>> handlers, List<?> parameters) {
        logger.info(command);
        if (CollectionUtils.isEmpty(handlers)) {
            return update(source, command);
        }
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = source.getConnection();
            statement = connection.prepareStatement(command);
            int i = 0;
            for (JdbcHandler<?> handler : handlers) {
                handler.setParameter(statement, i + 1, parameters.get(i));
                i++;
            }
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(statement);
            close(connection);
        }
    }

    protected List<T> select(DataSource source, String command) {
        logger.info(command);
        Connection connection = null;
        Statement statement = null;
        try {
            connection = source.getConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(command);
            return read(resultSet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(statement);
            close(connection);
        }
    }

    protected List<T> select(DataSource source, String command, List<JdbcHandler<?>> handlers, List<Object> parameters) {
        logger.info(command);
        if (CollectionUtils.isEmpty(handlers)) {
            return select(source, command);
        }
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = source.getConnection();
            statement = connection.prepareStatement(command);
            int i = 0;
            for (JdbcHandler<?> handler : handlers) {
                handler.setParameter(statement, i + 1, parameters.get(i));
                i++;
            }
            ResultSet resultSet = statement.executeQuery();
            return read(resultSet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(statement);
            close(connection);
        }
    }

    protected String buildCreateCommand(T document, String database, String datatable, List<JdbcHandler<?>> handlers, List<Object> parameters) {
        List<String> propertyNames = new LinkedList<>();

        List<JdbcProperty> properties = this.metadata.getProperties();
        for (JdbcProperty property : properties) {
            Object value = property.getValue(document);
            if (value != null) {
                parameters.add(value);
                handlers.add(property.getHandler());
                propertyNames.add(property.getName());
            }
        }

        if (propertyNames.isEmpty()) {
            throw new RuntimeException();
        }

        String columns = StringUtils.join(propertyNames, ", ");
        String[] repeat = StringUtils.repeat("?", properties.size());
        String values = StringUtils.join(repeat, ", ");

        return StringUtils.format(CREATE_SQL, database, datatable, columns, values);
    }

    protected String buildUpdateCommand(T document, String database, String datatable, List<JdbcHandler<?>> handlers, List<Object> parameters) {
        List<String> propertyNames = new LinkedList<>();

        List<JdbcProperty> properties = this.metadata.getProperties();
        for (JdbcProperty property : properties) {
            if (property.equals(primaryKey)) {
                continue;
            }
            Object value = property.getValue(document);
            if (value != null) {
                parameters.add(value);
                handlers.add(property.getHandler());
                propertyNames.add(property.getName() + " = ?");
            }
        }

        if (propertyNames.isEmpty()) {
            throw new RuntimeException();
        }

        handlers.add(primaryKey.getHandler());
        parameters.add(primaryKey.getValue(document));


        String property = StringUtils.join(propertyNames, ", ");

        String whereExpression = primaryKey.getColumn() + " = ?";

        return StringUtils.format(UPDATE_SQL, database, datatable, property, whereExpression);
    }

    protected String buildSelectCommand(String database, String datatable) {
        List<String> columnNames = this.metadata.getProperties().stream().map(JdbcProperty::getColumn).collect(Collectors.toList());
        String columns = StringUtils.join(columnNames, ", ");

        return StringUtils.format(SELECT_ALL_SQL, columns, database, datatable);
    }

    protected String buildSelectCommand(K id, String database, String datatable, List<JdbcHandler<?>> handlers, List<Object> parameters) {
        parameters.add(id);
        handlers.add(primaryKey.getHandler());
        String whereExpression = primaryKey.getColumn() + " = ?";
        String selectCommand = buildSelectCommand(database, datatable);
        return StringUtils.format(WHERE, selectCommand, whereExpression);
    }

    protected String buildDeleteCommand(K id, String database, String datatable, List<JdbcHandler<?>> handlers, List<Object> parameters) {
        parameters.add(id);
        assert primaryKey != null;
        handlers.add(primaryKey.getHandler());
        String whereExpression = primaryKey.getColumn() + " = ?";
        return StringUtils.format(DELETE_SQL, database, datatable, whereExpression);
    }

    protected String buildUpdateCommand(Update update, Criteria criteria, String database, String datatable, List<JdbcHandler<?>> handlers, List<Object> parameters) {
        MysqlTransfer mysqlTransfer = MysqlTransfer.builder().update(update).criteria(criteria).build().resolve();
        handlers.addAll(mysqlTransfer.getJdbcHandlers());
        parameters.addAll(mysqlTransfer.getParameters());
        return StringUtils.format(UPDATE_SQL, database, datatable, mysqlTransfer.getUpdateExpression(), mysqlTransfer.getWhereExpression());
    }

    protected String buildDeleteCommand(Criteria criteria, String database, String datatable, List<JdbcHandler<?>> handlers, List<Object> parameters) {
        MysqlTransfer mysqlTransfer = MysqlTransfer.builder().criteria(criteria).build().resolve();
        handlers.addAll(mysqlTransfer.getJdbcHandlers());
        parameters.addAll(mysqlTransfer.getParameters());
        return StringUtils.format(DELETE_SQL, database, datatable, mysqlTransfer.getWhereExpression());
    }

    protected String buildSelectCommand(Criteria criteria, String database, String datatable, List<JdbcHandler<?>> handlers, List<Object> parameters) {
        MysqlTransfer mysqlTransfer = MysqlTransfer.builder().metadata(this.metadata).criteria(criteria).build().resolve();
        handlers.addAll(mysqlTransfer.getJdbcHandlers());
        parameters.addAll(mysqlTransfer.getParameters());
        String selectCommand = buildSelectCommand(database, datatable);
        return StringUtils.format(WHERE, selectCommand, mysqlTransfer.getWhereExpression());
    }

}

package org.tinger.jdbc.repository;

import org.springframework.beans.factory.InitializingBean;
import org.tinger.jdbc.core.JdbcDriver;
import org.tinger.jdbc.dialect.JdbcDialect;
import org.tinger.jdbc.dialect.JdbcDialectFactory;
import org.tinger.jdbc.dialect.JdbcExecuteContext;
import org.tinger.jdbc.handler.JdbcHandler;
import org.tinger.jdbc.metadata.JdbcMetadata;
import org.tinger.jdbc.metadata.JdbcMetadataFactory;
import org.tinger.jdbc.metadata.JdbcProperty;

import javax.sql.DataSource;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by tinger on 2022-10-17
 */
public abstract class AbstractJdbcRepository<T, K> implements InitializingBean {

    protected JdbcMetadata<T, K> metadata;
    protected JdbcDialect dialect;
    private final Logger logger = Logger.getLogger(getDriver().name());


    public abstract JdbcDriver getDriver();

    @Override
    public void afterPropertiesSet() {
        this.metadata = JdbcMetadataFactory.getInstance().build(this);
        this.dialect = JdbcDialectFactory.getInstance().getDialect(this.getDriver());
    }

    @SuppressWarnings("unchecked")
    public K getId(T document) {
        Object value = this.metadata.getPrimaryKey().getValue(document);
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

    private void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                // ignore
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

    protected int update(DataSource source, JdbcExecuteContext<T, K> context) {
        return this.update(source, context.getCommand(), context.getHandlers(), context.getParameters());
    }

    protected List<T> select(DataSource source, String command) {
        logger.info(command);
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = source.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(command);
            return read(resultSet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(resultSet);
            close(statement);
            close(connection);
        }
    }

    protected List<T> select(DataSource source, String command, List<JdbcHandler<?>> handlers, List<Object> parameters) {
        logger.info(command);
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = source.getConnection();
            statement = connection.prepareStatement(command);
            int i = 0;
            for (JdbcHandler<?> handler : handlers) {
                handler.setParameter(statement, i + 1, parameters.get(i));
                i++;
            }
            resultSet = statement.executeQuery();
            return read(resultSet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(resultSet);
            close(statement);
            close(connection);
        }
    }

    protected List<T> select(DataSource datasource, JdbcExecuteContext<T, K> context) {
        return this.select(datasource, context.getCommand(), context.getHandlers(), context.getParameters());
    }
}
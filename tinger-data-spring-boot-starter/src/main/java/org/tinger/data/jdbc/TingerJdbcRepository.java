package org.tinger.data.jdbc;

import org.springframework.jdbc.core.StatementCallback;
import org.tinger.common.utils.StringUtils;
import org.tinger.data.core.meta.TingerNamespace;
import org.tinger.data.core.meta.TingerProperty;
import org.tinger.data.core.repo.AbstractRepository;
import org.tinger.data.core.tsql.Criteria;
import org.tinger.data.core.tsql.Update;
import org.tinger.data.jdbc.handler.JdbcHandler;
import org.tinger.data.jdbc.handler.JdbcHandlerHolder;
import org.tinger.data.jdbc.statement.StatementCreator;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public abstract class TingerJdbcRepository<T, K> extends AbstractRepository<T, K> {

    private final ResultSetResolver<T> resultSetResolver;

    protected TingerJdbcRepository() {
        super();
        this.resultSetResolver = prepareResolver();
    }

    protected ResultSetResolver<T> prepareResolver() {
        return new DefaultResultSetResolver<>(this.metadata);
    }

    protected List<T> select(DataSource source, String commandText) {
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try {
            connection = source.getConnection();
            statement = connection.createStatement();
            result = statement.executeQuery(commandText);
            return this.resultSetResolver.resolveList(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(result);
            close(statement);
            close(connection);
        }
    }

    protected List<T> select(DataSource source, JdbcExecuteContext<T, K> context) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            connection = source.getConnection();
            statement = connection.prepareStatement(context.getCommandText());
            appendParameter(statement, context);
            result = statement.executeQuery();
            return this.resultSetResolver.resolveList(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(result);
            close(statement);
            close(connection);
        }
    }

    protected int update(DataSource source, JdbcExecuteContext<T, K> context) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = source.getConnection();
            statement = connection.prepareStatement(context.getCommandText());
            appendParameter(statement, context);
            return statement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(statement);
            close(connection);
        }
    }

    protected void appendParameter(PreparedStatement statement, JdbcExecuteContext<T, K> context) throws Exception {
        List<JdbcHandler<?>> handlers = context.getHandlers();
        List<Object> parameters = context.getParameters();
        for (int i = 0; i < handlers.size(); i++) {
            handlers.get(i).setParameter(statement, i + 1, parameters.get(i));
        }
    }

    protected int updateBatch(DataSource source, JdbcExecuteContext<T, K> context) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = source.getConnection();
            connection.setAutoCommit(false);
            try {
                List<JdbcHandler<?>> handlers = context.getHandlers();
                statement = connection.prepareStatement(context.getCommandText());
                for (List<Object> parameterValues : context.getParametersList()) {
                    for (int i = 0; i < handlers.size(); i++) {
                        handlers.get(i).setParameter(statement, i + 1, parameterValues.get(i));
                    }
                    statement.addBatch();
                }
                int[] ints = statement.executeBatch();
                boolean success = Arrays.stream(ints).allMatch(x -> x > 0);
                if (success) {
                    connection.commit();
                    return context.getParameters().size();
                } else {
                    connection.rollback();
                    return 0;
                }
            } catch (Exception e) {
                connection.rollback();
                return 0;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(statement);
            close(connection);
        }
    }

    protected List<T> selectBatch(DataSource source, JdbcExecuteContext<T, K> context) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = source.getConnection();
            connection.setAutoCommit(false);
            List<JdbcHandler<?>> handlers = context.getHandlers();
            statement = connection.prepareStatement(context.getCommandText());
            List<T> results = new LinkedList<>();
            for (List<Object> parameterValues : context.getParametersList()) {
                for (int i = 0; i < handlers.size(); i++) {
                    handlers.get(i).setParameter(statement, i + 1, parameterValues.get(i));
                }
                try {
                    resultSet = statement.executeQuery();
                    List<T> ts = resultSetResolver.resolveList(resultSet);
                    results.addAll(ts);
                } finally {
                    close(resultSet);
                }

                statement.clearParameters();
            }
            return results;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(statement);
            close(connection);
        }
    }

    protected JdbcExecuteContext<T, K> buildCreateContext(String databaseName, String datatableName) {
        JdbcHandlerHolder jdbcHandlerHolder = JdbcHandlerHolder.getInstance();
        int size = metadata.getProperties().size();
        List<TingerProperty> properties = new ArrayList<>(size + 1);
        List<JdbcHandler<?>> jdbcHandlers = new ArrayList<>(size + 1);

        TingerProperty primaryKey = metadata.getPrimaryKey();
        properties.add(primaryKey);
        jdbcHandlers.add(jdbcHandlerHolder.get(primaryKey.getType()));

        List<TingerProperty> ps = metadata.getProperties();
        for (TingerProperty p : ps) {
            properties.add(p);
            jdbcHandlers.add(jdbcHandlerHolder.get(p.getType()));
        }

        String columnNames = StringUtils.join(properties.stream().map(x -> "`" + x.getColumn() + "`").toList(), ", ");
        String parameters = StringUtils.repeat("?", ", ", properties.size());

        String template = "INSERT INTO `[]`.`[]`([]) VALUES([])";
        String commandText = StringUtils.format(template, databaseName, datatableName, columnNames, parameters);

        return JdbcExecuteContext.<T, K>builder().commandText(commandText).handlers(jdbcHandlers).properties(properties).build();
    }

    protected JdbcExecuteContext<T, K> buildUpdateContext(String databaseName, String datatableName) {
        JdbcHandlerHolder jdbcHandlerHolder = JdbcHandlerHolder.getInstance();
        int size = metadata.getProperties().size();
        List<TingerProperty> properties = new ArrayList<>(size + 1);
        List<JdbcHandler<?>> jdbcHandlers = new ArrayList<>(size + 1);

        List<TingerProperty> ps = metadata.getProperties();
        for (TingerProperty p : ps) {
            properties.add(p);
            jdbcHandlers.add(jdbcHandlerHolder.get(p.getType()));
        }

        TingerProperty primaryKey = metadata.getPrimaryKey();
        properties.add(primaryKey);
        jdbcHandlers.add(jdbcHandlerHolder.get(primaryKey.getType()));

        String columnNames = StringUtils.join(metadata.getProperties().stream().map(x -> "`" + x.getColumn() + "` = ?").toList(), ", ");

        String template = "UPDATE `[]`.`[]` SET [] WHERE `[]` = ?";
        String commandText = StringUtils.format(template, databaseName, datatableName, columnNames, primaryKey.getColumn());

        return JdbcExecuteContext.<T, K>builder().commandText(commandText).handlers(jdbcHandlers).properties(properties).build();
    }

    protected JdbcExecuteContext<T, K> buildDeleteContext(String databaseName, String datatableName) {
        TingerProperty primaryKey = metadata.getPrimaryKey();
        List<TingerProperty> properties = Collections.singletonList(primaryKey);
        List<JdbcHandler<?>> jdbcHandlers = Collections.singletonList(JdbcHandlerHolder.getInstance().get(primaryKey.getType()));
        String commandText = StringUtils.format("DELETE FROM `[]`.`[]` WHERE `[]` = ?", databaseName, datatableName, primaryKey.getColumn());
        return JdbcExecuteContext.<T, K>builder().commandText(commandText).handlers(jdbcHandlers).properties(properties).build();
    }

    protected JdbcExecuteContext<T, K> buildSelectContext(String databaseName, String datatableName) {
        TingerProperty primaryKey = metadata.getPrimaryKey();
        List<TingerProperty> properties = Collections.singletonList(primaryKey);
        List<JdbcHandler<?>> handlers = Collections.singletonList(JdbcHandlerHolder.getInstance().get(primaryKey.getType()));
        List<String> names = new LinkedList<>();
        names.add(primaryKey.getColumn());
        metadata.getProperties().forEach(x -> names.add(x.getColumn()));
        String columnNames = StringUtils.join(names, ", ");
        String commandText = StringUtils.format("SELECT [] FROM `[]`.`[]` WHERE `[]` = ?", databaseName, datatableName, columnNames, primaryKey.getColumn());
        return JdbcExecuteContext.<T, K>builder().commandText(commandText).handlers(handlers).properties(properties).build();
    }

    protected JdbcExecuteContext<T, K> buildQueryableContext(Criteria criteria, String databaseName, String datatableName) {
        String commandText = buildQueryCommandText(databaseName, datatableName);
        JdbcTransfer transfer = JdbcTransfer.builder().metadata(metadata).criteria(criteria).build().resolve();
        String queryCommandText = StringUtils.format("[] WHERE []", commandText, transfer.getWhereExpression());
        return JdbcExecuteContext.<T, K>builder().commandText(queryCommandText).parameters(transfer.getParameters()).handlers(transfer.getHandlers()).build();
    }

    protected JdbcExecuteContext<T, K> buildUpdateContext(Update update, Criteria criteria, String databaseName, String datatableName) {
        JdbcTransfer transfer = JdbcTransfer.builder().metadata(metadata).criteria(criteria).update(update).build().resolve();
        String updateCommandText = StringUtils.format("UPDATE `[]`.`[]` SET [] WHERE []", databaseName, datatableName, transfer.getUpdateExpression(), transfer.getWhereExpression());
        return JdbcExecuteContext.<T, K>builder().commandText(updateCommandText).parameters(transfer.getParameters()).handlers(transfer.getHandlers()).build();
    }

    protected String buildQueryCommandText(String databaseName, String datatableName) {
        List<String> columnNames = new LinkedList<>();
        columnNames.add(metadata.getPrimaryKey().getColumn());
        for (TingerProperty property : metadata.getProperties()) {
            columnNames.add(property.getColumn());
        }
        String columns = StringUtils.join(columnNames, ", ");


        String template = "SELECT [] FROM `[]`.`[]`";
        return StringUtils.format(template, columns, databaseName, datatableName);
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
}
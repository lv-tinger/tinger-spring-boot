package org.tinger.data.jdbc;

import org.tinger.data.core.meta.TingerProperty;
import org.tinger.data.core.repo.AbstractRepository;
import org.tinger.data.jdbc.handler.JdbcHandler;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public abstract class TingerJdbcRepository<T, K> extends AbstractRepository<T, K> {

    private ResultSetResolver resultSetResolver;

    protected TingerJdbcRepository() {
        super();
        prepareResolver();
    }

    private void prepareResolver() {
        ArrayList<TingerProperty> properties = new ArrayList<>();
        properties.add(this.metadata.getPrimaryKey());
        properties.addAll(this.metadata.getProperties());
        this.resultSetResolver = ResultSetResolver.builder().constructor(this.metadata.getConstructor()).properties(properties).build();
    }

    protected List<T> select(DataSource source, String command) {
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try {
            connection = source.getConnection();
            statement = connection.createStatement();
            result = statement.executeQuery(command);
            return resolve(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(result);
            close(statement);
            close(connection);
        }
    }

    protected int update(DataSource source, String command) {
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

    protected List<T> select(DataSource source, String command, List<JdbcHandler<?>> parameterHandlers, List<Object> parameterValues) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            connection = source.getConnection();
            statement = connection.prepareStatement(command);
            for (int i = 0; i < parameterHandlers.size(); i++) {
                parameterHandlers.get(i).setParameter(statement, i + 1, parameterValues.get(i));
            }
            result = statement.executeQuery();
            return resolve(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(result);
            close(statement);
            close(connection);
        }
    }

    protected int update(DataSource source, String command, List<JdbcHandler<?>> parameterHandlers, List<Object> parameterValues) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = source.getConnection();
            statement = connection.prepareStatement(command);
            for (int i = 0; i < parameterHandlers.size(); i++) {
                parameterHandlers.get(i).setParameter(statement, i + 1, parameterValues.get(i));
            }
            return statement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(statement);
            close(connection);
        }
    }

    protected int updateBatch(DataSource source, String command, List<JdbcHandler<?>> parameterHandlers, List<List<Object>> parameterValuesList) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = source.getConnection();
            connection.setAutoCommit(false);
            try {
                statement = connection.prepareStatement(command);
                for (List<Object> parameterValues : parameterValuesList) {
                    for (int i = 0; i < parameterHandlers.size(); i++) {
                        parameterHandlers.get(i).setParameter(statement, i + 1, parameterValues.get(i));
                    }
                    statement.addBatch();
                }
                int[] ints = statement.executeBatch();
                boolean success = Arrays.stream(ints).allMatch(x -> x > 0);
                if (success) {
                    connection.commit();
                    return parameterValuesList.size();
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

    private List<T> resolve(ResultSet resultSet) throws Exception {
        List<TingerProperty> properties = metadata.getProperties();

        LinkedList<T> list = new LinkedList<>();
        while (resultSet.next()) {
            T item = (T) metadata.getConstructor().newInstance();
            list.add(item);
            int index = 1;

            for (TingerProperty property : properties) {
                Object value = property.getHandler().getResult(resultSet, index + 1);
                if (value != null) {
                    property.setValue(item, value);
                }
                index += 1;
            }
        }
        return list;
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

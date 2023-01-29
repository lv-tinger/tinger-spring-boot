package org.tinger.data.jdbc.repository;

import org.tinger.data.core.repo.AbstractRepository;
import org.tinger.data.jdbc.JdbcUtils;
import org.tinger.data.jdbc.ResultSetResolver;
import org.tinger.data.jdbc.statement.StatementCreator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;

public abstract class TingerJdbcRepository<T, K> extends AbstractRepository<T, K> {
    protected int execute(JdbcNamespace namespace, StatementCreator creator, Object parameter) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = namespace.getSource().getConnection();
            statement = creator.statement(connection, namespace.getDatabase(), namespace.getDatatable(), parameter);
            return statement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException();
        } finally {
            JdbcUtils.close(statement);
            JdbcUtils.close(connection);
        }
    }

    protected int executeBatch(JdbcNamespace namespace, StatementCreator creator, Object parameter) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = namespace.getSource().getConnection();
            statement = creator.statement(connection, namespace.getDatabase(), namespace.getDatatable(), parameter);
            connection.setAutoCommit(false);
            try {
                int[] ints = statement.executeBatch();
                connection.commit();
                return Arrays.stream(ints).sum();
            } catch (Exception e) {
                connection.rollback();
                return 0;
            }
        } catch (Exception e) {
            throw new RuntimeException();
        } finally {
            JdbcUtils.close(statement);
            JdbcUtils.close(connection);
        }
    }

    protected List<T> select(JdbcNamespace namespace, StatementCreator creator, ResultSetResolver<T> resolver, Object parameter) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = namespace.getSource().getConnection();
            statement = creator.statement(connection, namespace.getDatabase(), namespace.getDatatable(), parameter);
            resultSet = statement.executeQuery();
            return resolver.resolveList(resultSet);
        } catch (Exception e) {
            throw new RuntimeException();
        } finally {
            JdbcUtils.close(resultSet);
            JdbcUtils.close(statement);
            JdbcUtils.close(connection);
        }
    }
}
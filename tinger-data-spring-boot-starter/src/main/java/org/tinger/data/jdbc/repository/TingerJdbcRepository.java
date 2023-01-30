package org.tinger.data.jdbc.repository;

import org.tinger.data.core.repo.AbstractRepository;
import org.tinger.data.jdbc.JdbcUtils;
import org.tinger.data.jdbc.resolver.TingerResolver;
import org.tinger.data.jdbc.statement.StatementCreator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;

public abstract class TingerJdbcRepository<T, K> extends AbstractRepository<T, K> {
    protected int update(DataSource source, StatementCreator creator) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = source.getConnection();
            statement = creator.statement(connection);
            return statement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException();
        } finally {
            JdbcUtils.close(statement);
            JdbcUtils.close(connection);
        }
    }

    protected int batch(DataSource source, StatementCreator creator) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = source.getConnection();
            connection.setAutoCommit(false);
            statement = creator.statement(connection);
            try {
                int[] counts = statement.executeBatch();
                connection.commit();
                return Arrays.stream(counts).sum();
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

    protected <TK> TK query(DataSource source, StatementCreator creator, TingerResolver<TK> resolver) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = source.getConnection();
            statement = creator.statement(connection);
            resultSet = statement.executeQuery();
            return resolver.resolve(resultSet);
        } catch (Exception e) {
            throw new RuntimeException();
        } finally {
            JdbcUtils.close(resultSet);
            JdbcUtils.close(statement);
            JdbcUtils.close(connection);
        }
    }
}
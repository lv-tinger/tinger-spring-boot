package org.tinger.data.jdbc.repository;

import org.tinger.data.core.repo.AbstractRepository;
import org.tinger.data.jdbc.resolver.TingerResolver;
import org.tinger.data.jdbc.statement.StatementBatchCreator;
import org.tinger.data.jdbc.statement.StatementCreator;
import org.tinger.data.jdbc.utils.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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

    protected int updateBatch(DataSource source, StatementCreator creator) {
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

    protected List<T> queryBatch(DataSource source, StatementBatchCreator creator, TingerResolver<List<T>> resolver) {
        Connection connection = null;
        List<PreparedStatement> statements = null;
        ResultSet resultSet = null;
        try {
            connection = source.getConnection();
            statements = creator.statement(connection);
            List<T> results = new LinkedList<>();
            for (PreparedStatement statement : statements) {
                resultSet = statement.executeQuery();
                List<T> resolve = resolver.resolve(resultSet);
                results.addAll(resolve);
            }
            return results;
        } catch (Exception e) {
            throw new RuntimeException();
        } finally {
            JdbcUtils.close(resultSet);
            if (statements != null) {
                statements.forEach(JdbcUtils::close);
            }
            JdbcUtils.close(connection);
        }
    }
}
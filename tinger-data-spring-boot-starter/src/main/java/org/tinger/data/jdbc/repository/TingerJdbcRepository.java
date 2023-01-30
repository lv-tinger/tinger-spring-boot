package org.tinger.data.jdbc.repository;

import org.tinger.data.core.repo.AbstractRepository;
import org.tinger.data.jdbc.JdbcUtils;
import org.tinger.data.jdbc.resolver.TingerResolver;
import org.tinger.data.jdbc.resolver.template.DefaultDocumentJdbcResolverTemplate;
import org.tinger.data.jdbc.resolver.template.TingerDocumentJdbcResolverTemplate;
import org.tinger.data.jdbc.statement.StatementCreator;
import org.tinger.data.jdbc.statement.template.DocumentCreateStatementCreatorTemplate;
import org.tinger.data.jdbc.statement.template.DocumentDeleteStatementCreatorTemplate;
import org.tinger.data.jdbc.statement.template.DocumentSelectStatementCreatorTemplate;
import org.tinger.data.jdbc.statement.template.DocumentUpdateStatementCreatorTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;

public abstract class TingerJdbcRepository<T, K> extends AbstractRepository<T, K> {
    private int update(DataSource source, StatementCreator creator) {
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

    private int batch(DataSource source, StatementCreator creator) {
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

    private <K> K query(DataSource source, StatementCreator creator, TingerResolver<K> resolver) {
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

    protected int create(JdbcNamespace namespace, T document) {
        StatementCreator creator = connection -> DocumentCreateStatementCreatorTemplate.build(metadata).statement(connection, namespace.getDatabase(), namespace.getDatatable(), document);
        return update(namespace.getSource(), creator);
    }

    protected int update(JdbcNamespace namespace, T document) {
        StatementCreator creator = connection -> DocumentUpdateStatementCreatorTemplate.build(metadata).statement(connection, namespace.getDatabase(), namespace.getDatatable(), document);
        return update(namespace.getSource(), creator);
    }

    protected int delete(JdbcNamespace namespace, K id) {
        StatementCreator creator = connection -> DocumentDeleteStatementCreatorTemplate.build(metadata).statement(connection, namespace.getDatabase(), namespace.getDatatable(), id);
        return update(namespace.getSource(), creator);
    }

    protected T select(JdbcNamespace namespace, K id) {
        StatementCreator creator = connection -> DocumentSelectStatementCreatorTemplate.build(metadata).statement(connection, namespace.getDatabase(), namespace.getDatatable(), id);
        TingerResolver<T> resolver = new TingerResolver<>() {
            @Override
            public <T> T resolve(ResultSet set) throws Exception {
                TingerDocumentJdbcResolverTemplate<T> template = (TingerDocumentJdbcResolverTemplate<T>) DefaultDocumentJdbcResolverTemplate.build(metadata);
                if (set.next()) {
                    return template.resolve(set);
                } else {
                    return null;
                }
            }
        };
        return query(namespace.getSource(), creator, resolver);
    }
}
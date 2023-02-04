package org.tinger.data.jdbc.repository;

import lombok.Getter;
import lombok.Setter;
import org.tinger.data.core.tsql.Queryable;
import org.tinger.data.jdbc.resolver.TingerResolver;
import org.tinger.data.jdbc.statement.StatementBatchCreator;
import org.tinger.data.jdbc.statement.StatementCreator;
import org.tinger.data.jdbc.statement.template.document.*;
import org.tinger.data.jdbc.statement.template.queryable.SelectStatementCreatorTemplate;

import java.util.Arrays;
import java.util.List;

import static org.tinger.data.jdbc.resolver.template.DocumentJdbcResolverTemplateHolder.build;

@Getter
@Setter
public class JdbcSingletRepository<T extends Object, K> extends TingerJdbcRepository<T, K> {
    private JdbcNamespace namespace;

    public int create(T document) {
        StatementCreator creator = connection -> DocumentCreateStatementCreatorTemplate.build(metadata).statement(connection, namespace.getDatabase(), namespace.getDatatable(), document);
        return update(namespace.getSource(), creator);
    }

    public int create(List<T> documents) {
        StatementCreator creator = connection -> DocumentCreateStatementCreatorTemplate.build(metadata).statement(connection, namespace.getDatabase(), namespace.getDatatable(), documents);
        return updateBatch(namespace.getSource(), creator);
    }

    public int update(T document) {
        StatementCreator creator = connection -> DocumentUpdateStatementCreatorTemplate.build(metadata).statement(connection, namespace.getDatabase(), namespace.getDatatable(), document);
        return update(namespace.getSource(), creator);
    }

    public int update(List<T> documents) {
        StatementCreator creator = connection -> DocumentUpdateStatementCreatorTemplate.build(metadata).statement(connection, namespace.getDatabase(), namespace.getDatatable(), documents);
        return updateBatch(namespace.getSource(), creator);
    }

    public int delete(K id) {
        StatementCreator creator = connection -> DocumentDeleteStatementCreatorTemplate.build(metadata).statement(connection, namespace.getDatabase(), namespace.getDatatable(), id);
        return update(namespace.getSource(), creator);
    }

    public int delete(List<K> ids) {
        StatementCreator creator = connection -> DocumentDeleteStatementCreatorTemplate.build(metadata).statement(connection, namespace.getDatabase(), namespace.getDatatable(), ids);
        return updateBatch(namespace.getSource(), creator);
    }

    public T select(K id) {
        StatementCreator creator = connection -> DocumentSelectStatementCreatorTemplate.build(metadata).statement(connection, namespace.getDatabase(), namespace.getDatatable(), id);
        TingerResolver<T> resolver = set -> build(metadata).resolveOne(set);
        return query(namespace.getSource(), creator, resolver);
    }

    public List<T> select(List<K> ids) {
        StatementBatchCreator creator = connection -> DocumentSelectBatchStatementCreatorTemplate.build(metadata).statement(connection, namespace.getDatabase(), namespace.getDatatable(), ids);
        TingerResolver<List<T>> resolver = set -> build(metadata).resolveList(set);
        return queryBatch(namespace.getSource(), creator, resolver);
    }

    public List<T> select(Queryable queryable, List<Object> parameters) {
        StatementCreator creator = connection -> SelectStatementCreatorTemplate.build(queryable).statement(connection, namespace.getDatabase(), namespace.getDatatable(), parameters);
        TingerResolver<List<T>> resolver = set -> build(metadata).resolveList(set);
        return query(namespace.getSource(), creator, resolver);
    }

    public List<T> select(Queryable queryable, Object... parameters) {
        StatementCreator creator = connection -> SelectStatementCreatorTemplate.build(queryable).statement(connection, namespace.getDatabase(), namespace.getDatatable(), parameters);
        TingerResolver<List<T>> resolver = set -> build(metadata).resolveList(set);
        return query(namespace.getSource(), creator, resolver);
    }
}
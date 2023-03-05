package org.tinger.data.jdbc.repository;

import lombok.Getter;
import lombok.Setter;
import org.tinger.data.core.tsql.Queryable;
import org.tinger.data.core.tsql.Updatable;
import org.tinger.data.jdbc.namespace.JdbcNamespace;
import org.tinger.data.jdbc.resolver.TingerResolver;
import org.tinger.data.jdbc.statement.StatementBatchCreator;
import org.tinger.data.jdbc.statement.StatementCreator;
import org.tinger.data.jdbc.statement.template.document.*;
import org.tinger.data.jdbc.statement.template.queryable.SelectStatementCreatorTemplate;
import org.tinger.data.jdbc.statement.template.queryable.UpdateStatementCreatorTemplate;

import javax.sql.DataSource;
import java.io.Serializable;
import java.util.List;

import static org.tinger.data.jdbc.resolver.template.DocumentJdbcResolverTemplateHolder.build;

@Getter
@Setter
public class JdbcSingletRepository<T extends Serializable, K> extends TingerJdbcRepository<T, K> {
    private JdbcNamespace namespace;

    public int create(T document) {
        StatementCreator creator = connection -> DocumentCreateStatementCreatorTemplate.build(metadata).statement(connection, namespace.getDatabase(), namespace.getDatatable(), document);
        return update(namespace.getMaster(), creator);
    }

    public int create(List<T> documents) {
        StatementCreator creator = connection -> DocumentCreateStatementCreatorTemplate.build(metadata).statement(connection, namespace.getDatabase(), namespace.getDatatable(), documents);
        return updateBatch(namespace.getMaster(), creator);
    }

    public int update(T document) {
        StatementCreator creator = connection -> DocumentUpdateStatementCreatorTemplate.build(metadata).statement(connection, namespace.getDatabase(), namespace.getDatatable(), document);
        return update(namespace.getMaster(), creator);
    }

    public int update(List<T> documents) {
        StatementCreator creator = connection -> DocumentUpdateStatementCreatorTemplate.build(metadata).statement(connection, namespace.getDatabase(), namespace.getDatatable(), documents);
        return updateBatch(namespace.getMaster(), creator);
    }

    public int delete(K id) {
        StatementCreator creator = connection -> DocumentDeleteStatementCreatorTemplate.build(metadata).statement(connection, namespace.getDatabase(), namespace.getDatatable(), id);
        return update(namespace.getMaster(), creator);
    }

    public int delete(List<K> ids) {
        StatementCreator creator = connection -> DocumentDeleteStatementCreatorTemplate.build(metadata).statement(connection, namespace.getDatabase(), namespace.getDatatable(), ids);
        return updateBatch(namespace.getMaster(), creator);
    }

    public T select(K id) {
        StatementCreator creator = connection -> DocumentSelectStatementCreatorTemplate.build(metadata).statement(connection, namespace.getDatabase(), namespace.getDatatable(), id);
        TingerResolver<T> resolver = set -> build(metadata).resolveOne(set);
        return query(namespace.getMaster(), creator, resolver);
    }

    public T select(Preference preference, K id) {
        DataSource dataSource = preference == Preference.MASTER ? namespace.getMaster() : namespace.getSlaver();
        return select(dataSource, id);
    }

    private T select(DataSource source, K id) {
        StatementCreator creator = connection -> DocumentSelectStatementCreatorTemplate.build(metadata).statement(connection, namespace.getDatabase(), namespace.getDatatable(), id);
        TingerResolver<T> resolver = set -> build(metadata).resolveOne(set);
        return query(source, creator, resolver);
    }

    public List<T> select(List<K> ids) {
        StatementBatchCreator creator = connection -> DocumentSelectBatchStatementCreatorTemplate.build(metadata).statement(connection, namespace.getDatabase(), namespace.getDatatable(), ids);
        TingerResolver<List<T>> resolver = set -> build(metadata).resolveList(set);
        return queryBatch(namespace.getMaster(), creator, resolver);
    }

    public List<T> select(Preference preference, List<K> ids) {
        DataSource dataSource = preference == Preference.MASTER ? namespace.getMaster() : namespace.getSlaver();
        return select(dataSource, ids);
    }

    private List<T> select(DataSource source, List<K> ids) {
        StatementBatchCreator creator = connection -> DocumentSelectBatchStatementCreatorTemplate.build(metadata).statement(connection, namespace.getDatabase(), namespace.getDatatable(), ids);
        TingerResolver<List<T>> resolver = set -> build(metadata).resolveList(set);
        return queryBatch(source, creator, resolver);
    }

    public List<T> select(Queryable queryable, Object... parameters) {
        return select(namespace.getMaster(), queryable, parameters);
    }

    public List<T> select(Preference preference, Queryable queryable, Object... parameters) {
        DataSource dataSource = preference == Preference.MASTER ? namespace.getMaster() : namespace.getSlaver();
        return select(dataSource, queryable, parameters);
    }

    private List<T> select(DataSource source, Queryable queryable, Object... parameters) {
        StatementCreator creator = connection -> SelectStatementCreatorTemplate.build(queryable)
                .statement(connection, namespace.getDatabase(), namespace.getDatatable(), parameters);
        TingerResolver<List<T>> resolver = set -> build(metadata).resolveList(set);
        return query(source, creator, resolver);
    }

    public int update(Updatable updatable, List<Object> parameters) {
        StatementCreator creator = connection -> UpdateStatementCreatorTemplate.build(updatable).statement(connection, namespace.getDatabase(), namespace.getDatatable(), parameters);
        return update(namespace.getMaster(), creator);
    }

    public int update(Updatable updatable, Object... parameters) {
        StatementCreator creator = connection -> UpdateStatementCreatorTemplate.build(updatable).statement(connection, namespace.getDatabase(), namespace.getDatatable(), parameters);
        return update(namespace.getMaster(), creator);
    }
}
package org.tinger.data.jdbc.repository;

import org.tinger.data.core.tsql.Queryable;
import org.tinger.data.core.tsql.Updatable;
import org.tinger.data.jdbc.namespace.JdbcNamespace;
import org.tinger.data.jdbc.resolver.TingerResolver;
import org.tinger.data.jdbc.statement.StatementBatchCreator;
import org.tinger.data.jdbc.statement.StatementCreator;
import org.tinger.data.jdbc.statement.template.document.*;
import org.tinger.data.jdbc.statement.template.queryable.SelectStatementCreatorTemplate;
import org.tinger.data.jdbc.statement.template.queryable.UpdateStatementCreatorTemplate;
import org.tinger.data.jdbc.support.Preference;

import javax.sql.DataSource;
import java.io.Serializable;
import java.util.List;

import static org.tinger.data.jdbc.resolver.template.DocumentJdbcResolverTemplateHolder.build;

public abstract class JdbcShardRepository<T extends Serializable, K> extends TingerJdbcRepository<T, K> {
    private JdbcNamespace namespace;

    protected abstract String calculateDatabase(T document);

    protected abstract String calculateDatatable(T document);

    public int create(T document) {
        StatementCreator creator = connection -> DocumentCreateStatementCreatorTemplate.build(metadata)
                .statement(connection, calculateDatabase(document), calculateDatatable(document), document);
        return update(namespace.getMaster(), creator);
    }

    public int create(List<T> documents) {
        StatementCreator creator = connection -> DocumentCreateStatementCreatorTemplate.build(metadata)
                .statement(connection, calculateDatabase(documents.get(0)), calculateDatatable(documents.get(0)), documents);
        return updateBatch(namespace.getMaster(), creator);
    }

    public int update(T document) {
        StatementCreator creator = connection -> DocumentUpdateStatementCreatorTemplate.build(metadata)
                .statement(connection, calculateDatabase(document), calculateDatatable(document), document);
        return update(namespace.getMaster(), creator);
    }

    public int update(List<T> documents) {
        StatementCreator creator = connection -> DocumentUpdateStatementCreatorTemplate.build(metadata)
                .statement(connection, calculateDatabase(documents.get(0)), calculateDatatable(documents.get(0)), documents);
        ;
        return updateBatch(namespace.getMaster(), creator);
    }

    public int delete(K id, T mock) {
        StatementCreator creator = connection -> DocumentDeleteStatementCreatorTemplate.build(metadata)
                .statement(connection, calculateDatabase(mock), calculateDatatable(mock), id);
        return update(namespace.getMaster(), creator);
    }

    public int delete(List<K> ids, T mock) {
        StatementCreator creator = connection -> DocumentDeleteStatementCreatorTemplate.build(metadata)
                .statement(connection, calculateDatabase(mock), calculateDatatable(mock), ids);
        return updateBatch(namespace.getMaster(), creator);
    }

    public T select(K id, T mock) {
        StatementCreator creator = connection -> DocumentSelectStatementCreatorTemplate.build(metadata)
                .statement(connection, calculateDatabase(mock), calculateDatatable(mock), id);
        TingerResolver<T> resolver = set -> build(metadata).resolveOne(set);
        return query(namespace.getMaster(), creator, resolver);
    }

    public T select(Preference preference, K id, T mock) {
        DataSource source = preference == Preference.MASTER ? namespace.getMaster() : namespace.getSlaver();
        StatementCreator creator = connection -> DocumentSelectStatementCreatorTemplate.build(metadata)
                .statement(connection, calculateDatabase(mock), calculateDatatable(mock), id);
        TingerResolver<T> resolver = set -> build(metadata).resolveOne(set);
        return query(source, creator, resolver);
    }

    public List<T> select(T mock, List<K> ids) {
        StatementBatchCreator creator = connection -> DocumentSelectBatchStatementCreatorTemplate.build(metadata)
                .statement(connection, calculateDatabase(mock), calculateDatatable(mock), ids);
        TingerResolver<List<T>> resolver = set -> build(metadata).resolveList(set);
        return queryBatch(namespace.getMaster(), creator, resolver);
    }

    public List<T> select(T mock, Preference preference, List<K> ids) {
        DataSource source = preference == Preference.MASTER ? namespace.getMaster() : namespace.getSlaver();
        StatementBatchCreator creator = connection -> DocumentSelectBatchStatementCreatorTemplate.build(metadata)
                .statement(connection, calculateDatabase(mock), calculateDatatable(mock), ids);
        TingerResolver<List<T>> resolver = set -> build(metadata).resolveList(set);
        return queryBatch(source, creator, resolver);
    }

    public List<T> select(T mock, Queryable queryable, Object... parameters) {
        return select(mock, namespace.getMaster(), queryable, parameters);
    }

    public List<T> select(T mock, Preference preference, Queryable queryable, Object... parameters) {
        DataSource dataSource = preference == Preference.MASTER ? namespace.getMaster() : namespace.getSlaver();
        return select(mock, dataSource, queryable, parameters);
    }

    private List<T> select(T mock, DataSource source, Queryable queryable, Object... parameters) {
        StatementCreator creator = connection -> SelectStatementCreatorTemplate.build(queryable)
                .statement(connection, calculateDatabase(mock), calculateDatatable(mock), parameters);
        TingerResolver<List<T>> resolver = set -> build(metadata).resolveList(set);
        return query(source, creator, resolver);
    }

    public int update(T mock, Updatable updatable, List<Object> parameters) {
        StatementCreator creator = connection -> UpdateStatementCreatorTemplate.build(updatable)
                .statement(connection, calculateDatabase(mock), calculateDatatable(mock), parameters);
        return update(namespace.getMaster(), creator);
    }

    public int update(T mock, Updatable updatable, Object... parameters) {
        StatementCreator creator = connection -> UpdateStatementCreatorTemplate.build(updatable)
                .statement(connection, calculateDatabase(mock), calculateDatatable(mock), parameters);
        return update(namespace.getMaster(), creator);
    }
}

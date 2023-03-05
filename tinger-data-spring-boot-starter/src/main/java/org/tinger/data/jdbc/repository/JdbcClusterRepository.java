package org.tinger.data.jdbc.repository;

import lombok.Getter;
import lombok.Setter;
import org.tinger.data.core.tsql.Queryable;
import org.tinger.data.core.tsql.Updatable;
import org.tinger.data.jdbc.resolver.TingerResolver;
import org.tinger.data.jdbc.statement.StatementBatchCreator;
import org.tinger.data.jdbc.statement.StatementCreator;
import org.tinger.data.jdbc.statement.template.document.*;
import org.tinger.data.jdbc.statement.template.queryable.SelectStatementCreatorTemplate;
import org.tinger.data.jdbc.statement.template.queryable.UpdateStatementCreatorTemplate;

import javax.sql.DataSource;
import java.util.List;

import static org.tinger.data.jdbc.resolver.template.DocumentJdbcResolverTemplateHolder.build;

@Getter
@Setter
public class JdbcClusterRepository<T, K> extends TingerJdbcRepository<T, K> {
    private ShardAlgorithm<T> algorithm;

    public int create(T document) {
        StatementCreator creator = connection -> DocumentCreateStatementCreatorTemplate.build(metadata)
                .statement(connection, algorithm.database(document), algorithm.datatable(document), document);
        return update(algorithm.master(document), creator);
    }

    public int create(List<T> documents) {
        T document = documents.get(0);
        StatementCreator creator = connection -> DocumentCreateStatementCreatorTemplate.build(metadata)
                .statement(connection, algorithm.database(document), algorithm.datatable(document), documents);
        return update(algorithm.master(document), creator);
    }

    public int update(T document) {
        StatementCreator creator = connection -> DocumentUpdateStatementCreatorTemplate.build(metadata)
                .statement(connection, algorithm.database(document), algorithm.datatable(document), document);
        return update(algorithm.master(document), creator);
    }

    public int update(List<T> documents) {
        T document = documents.get(0);
        StatementCreator creator = connection -> DocumentUpdateStatementCreatorTemplate.build(metadata)
                .statement(connection, algorithm.database(document), algorithm.datatable(document), documents);
        return update(algorithm.master(document), creator);
    }

    public int delete(T mock, K id) {
        DataSource source = algorithm.master(mock);
        String database = algorithm.database(mock);
        String datatable = algorithm.datatable(mock);
        StatementCreator creator = connection -> DocumentDeleteStatementCreatorTemplate.build(metadata)
                .statement(connection, database, datatable, id);
        return update(source, creator);
    }

    public int delete(T mock, List<K> ids) {
        DataSource source = algorithm.master(mock);
        String database = algorithm.database(mock);
        String datatable = algorithm.datatable(mock);
        StatementCreator creator = connection -> DocumentDeleteStatementCreatorTemplate.build(metadata)
                .statement(connection, database, datatable, ids);
        return update(source, creator);
    }

    public T select(T mock, K id) {
        DataSource source = algorithm.master(mock);
        String database = algorithm.database(mock);
        String datatable = algorithm.datatable(mock);
        StatementCreator creator = connection -> DocumentSelectStatementCreatorTemplate.build(metadata)
                .statement(connection, database, datatable, id);
        TingerResolver<T> resolver = set -> build(metadata).resolveOne(set);
        return query(source, creator, resolver);
    }

    public T select(T mock, Preference preference, K id) {
        DataSource source = preference == Preference.MASTER ? algorithm.master(mock) : algorithm.slaver(mock);
        StatementCreator creator = connection -> DocumentSelectStatementCreatorTemplate.build(metadata)
                .statement(connection, algorithm.database(mock), algorithm.datatable(mock), id);
        TingerResolver<T> resolver = set -> build(metadata).resolveOne(set);
        return query(source, creator, resolver);
    }

    public List<T> select(T mock, List<K> ids) {
        StatementBatchCreator creator = connection -> DocumentSelectBatchStatementCreatorTemplate.build(metadata)
                .statement(connection, algorithm.database(mock), algorithm.datatable(mock), ids);
        TingerResolver<List<T>> resolver = set -> build(metadata).resolveList(set);
        return queryBatch(algorithm.master(mock), creator, resolver);
    }

    public List<T> select(T mock, Preference preference, List<K> ids) {
        DataSource source = preference == Preference.MASTER ? algorithm.master(mock) : algorithm.slaver(mock);
        StatementBatchCreator creator = connection -> DocumentSelectBatchStatementCreatorTemplate.build(metadata)
                .statement(connection, algorithm.database(mock), algorithm.datatable(mock), ids);
        TingerResolver<List<T>> resolver = set -> build(metadata).resolveList(set);
        return queryBatch(source, creator, resolver);
    }

    public List<T> select(T mock, Queryable queryable, Object... parameters) {
        String database = algorithm.database(mock);
        String datatable = algorithm.datatable(mock);
        return select(algorithm.master(mock), database, datatable, queryable, parameters);
    }

    public List<T> select(T mock, Preference preference, Queryable queryable, Object... parameters) {
        DataSource dataSource = preference == Preference.MASTER ? algorithm.master(mock) : algorithm.slaver(mock);
        String database = algorithm.database(mock);
        String datatable = algorithm.datatable(mock);
        return select(dataSource, database, datatable, queryable, parameters);
    }

    private List<T> select(DataSource source, String database, String datatable, Queryable queryable, Object... parameters) {
        StatementCreator creator = connection -> SelectStatementCreatorTemplate.build(queryable)
                .statement(connection, database, datatable, parameters);
        TingerResolver<List<T>> resolver = set -> build(metadata).resolveList(set);
        return query(source, creator, resolver);
    }

    public int update(T mock, Updatable updatable, List<Object> parameters) {
        String database = algorithm.database(mock);
        String datatable = algorithm.datatable(mock);
        StatementCreator creator = connection -> UpdateStatementCreatorTemplate.build(updatable)
                .statement(connection, database, datatable, parameters);
        return update(algorithm.master(mock), creator);
    }

    public int update(T mock, Updatable updatable, Object... parameters) {
        DataSource source = algorithm.master(mock);
        String database = algorithm.database(mock);
        String datatable = algorithm.datatable(mock);
        StatementCreator creator = connection -> UpdateStatementCreatorTemplate.build(updatable)
                .statement(connection, database, datatable, parameters);
        return update(source, creator);
    }
}

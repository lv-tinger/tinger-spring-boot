package org.tinger.jdbc.mysql;

import org.springframework.beans.factory.InitializingBean;
import org.tinger.common.utils.CollectionUtils;
import org.tinger.jdbc.anno.JdbcDataSource;
import org.tinger.jdbc.anno.JdbcDataTableName;
import org.tinger.jdbc.anno.JdbcDatabaseName;
import org.tinger.jdbc.core.JdbcEntity;
import org.tinger.jdbc.handler.JdbcHandler;
import org.tinger.jdbc.queryable.Criteria;
import org.tinger.jdbc.queryable.Update;
import org.tinger.jdbc.repository.ShardingRepository;
import org.tinger.jdbc.source.TingerJdbcDataSource;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by tinger on 2022-10-17
 */
public abstract class MysqlShardingRepository<T extends JdbcEntity<K>, S, K> extends AbstractMysqlRepository<T, K> implements ShardingRepository<T, S, K>, InitializingBean {
    private List<DataSource> sources;
    private String table;
    private String database;
    private TingerJdbcDataSource tingerJdbcDataSource;

    public int getSourceSize() {
        return this.sources.size();
    }

    @Override
    public void afterPropertiesSet() {
        this.table = this.getClass().getAnnotation(JdbcDataTableName.class).value();
        this.database = this.getClass().getAnnotation(JdbcDatabaseName.class).value();
        String sourceName = this.getClass().getAnnotation(JdbcDataSource.class).value();
        this.sources = tingerJdbcDataSource.shard(sourceName);
    }

    @Override
    public T select(S shard, K id) {
        List<JdbcHandler<?>> handlers = new LinkedList<>();
        List<Object> parameters = new LinkedList<>();
        String command = buildSelectCommand(id, calculateDatabase(shard), calculateDatatable(shard), handlers, parameters);
        List<T> list = this.select(calculateDatasource(shard), command, handlers, parameters);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    @Override
    public T create(S shard, T document) {
        List<JdbcHandler<?>> handlers = new LinkedList<>();
        List<Object> parameters = new LinkedList<>();
        String command = buildCreateCommand(document, calculateDatabase(shard), calculateDatatable(shard), handlers, parameters);
        int i = this.update(this.calculateDatasource(shard), command, handlers, parameters);
        if (i > 0) {
            return select(shard, getId(document));
        } else {
            return null;
        }
    }

    @Override
    public T update(S shard, T document) {
        List<JdbcHandler<?>> handlers = new LinkedList<>();
        List<Object> parameters = new LinkedList<>();
        String command = buildUpdateCommand(document, this.calculateDatabase(shard), this.calculateDatatable(shard), handlers, parameters);
        int i = this.update(this.calculateDatasource(shard), command, handlers, parameters);
        if (i > 0) {
            return select(shard, getId(document));
        } else {
            return null;
        }
    }

    @Override
    public List<T> update(S shard, Update update, Criteria criteria) {
        List<JdbcHandler<?>> handlers = new LinkedList<>();
        List<Object> parameters = new LinkedList<>();
        String command = buildUpdateCommand(update, criteria, calculateDatabase(shard), calculateDatatable(shard), handlers, parameters);
        int i = update(calculateDatasource(shard), command, handlers, parameters);
        if (i > 0) {
            return select(shard, criteria);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public T upsert(S shard, T document) {
        T instance = this.select(shard, getId(document));
        return instance == null ? create(shard, document) : update(shard, document);
    }

    @Override
    public T delete(S shard, T document) {
        T object = this.select(shard, getId(document));
        if (object == null) {
            return null;
        }
        List<JdbcHandler<?>> handlers = new LinkedList<>();
        List<Object> parameters = new LinkedList<>();
        String command = buildDeleteCommand(getId(document), calculateDatabase(shard), calculateDatatable(shard), handlers, parameters);
        int i = this.update(calculateDatasource(shard), command, handlers, parameters);
        if (i > 0) {
            return object;
        } else {
            return null;
        }
    }

    @Override
    public List<T> delete(S shard, Criteria criteria) {
        List<T> result = select(shard, criteria);
        if (result.size() > 0) {
            List<JdbcHandler<?>> handlers = new LinkedList<>();
            List<Object> parameters = new LinkedList<>();
            String command = buildDeleteCommand(criteria, calculateDatabase(shard), calculateDatatable(shard), handlers, parameters);
            update(calculateDatasource(shard), command, handlers, parameters);
        }
        return result;
    }

    @Override
    public List<T> select(S shard, Criteria criteria) {
        MysqlTransfer mysqlTransfer = MysqlTransfer.builder().metadata(this.metadata).criteria(criteria).build().resolve();
        String command = buildSelectCommand(calculateDatabase(shard), calculateDatatable(shard)) + " WHERE " + mysqlTransfer.getWhereExpression();
        return this.select(calculateDatasource(shard), command, mysqlTransfer.getJdbcHandlers(), mysqlTransfer.getParameters());
    }
}

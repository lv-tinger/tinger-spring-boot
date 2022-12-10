package org.tinger.jdbc.repository;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.tinger.common.utils.CollectionUtils;
import org.tinger.jdbc.core.JdbcEntity;
import org.tinger.jdbc.dialect.JdbcDialect;
import org.tinger.jdbc.dialect.JdbcExecuteContext;
import org.tinger.jdbc.queryable.Criteria;
import org.tinger.jdbc.queryable.Update;
import org.tinger.jdbc.source.TingerJdbcDataSource;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;

/**
 * Created by tinger on 2022-10-17
 */
public abstract class AbstractDynamicRepository<T extends JdbcEntity<K>, K> extends AbstractRepository<T, K> implements DynamicRepository<T, K>, InitializingBean {
    private List<DataSource> sources;
    private JdbcDialect dialect;
    @Autowired
    private TingerJdbcDataSource tingerJdbcDataSource;

    @Override
    public void afterPropertiesSet() {
        this.sources = tingerJdbcDataSource.shard(this.metadata.getDatasource());
    }


    @Override
    public T create(Object shard, T document) {
        JdbcExecuteContext<T, K> context = JdbcExecuteContext.<T, K>builder().metadata(this.metadata).document(document).build();
        this.dialect.resolveToCreateCommand(context);
        int index = this.metadata.getShardCalculator().calculateDatasource(shard);
        int i = this.update(this.sources.get(index), context);
        if (i > 0) {
            return select(shard, getId(document));
        } else {
            return null;
        }
    }

    @Override
    public T select(Object shard, K id) {
        JdbcExecuteContext<T, K> context = JdbcExecuteContext.<T, K>builder().metadata(this.metadata).primary(id).build();
        this.dialect.resolveToSelectCommand(context);
        int index = this.metadata.getShardCalculator().calculateDatasource(shard);
        List<T> list = this.select(this.sources.get(index), context);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    @Override
    public List<T> select(Object shard, Criteria criteria) {
        JdbcExecuteContext<T, K> context = JdbcExecuteContext.<T, K>builder().metadata(this.metadata).criteria(criteria).build();
        this.dialect.resolveToSelectCommand(context);
        int index = this.metadata.getShardCalculator().calculateDatasource(shard);
        return this.select(this.sources.get(index), context.getCommand(), context.getHandlers(), context.getParameters());
    }

    @Override
    public T update(Object shard, T document) {
        JdbcExecuteContext<T, K> context = JdbcExecuteContext.<T, K>builder().metadata(this.metadata).document(document).build();
        this.dialect.resolveToUpdateCommand(context);
        int index = this.metadata.getShardCalculator().calculateDatasource(shard);
        int i = this.update(this.sources.get(index), context.getCommand(), context.getHandlers(), context.getParameters());
        if (i > 0) {
            return select(shard, getId(document));
        } else {
            return null;
        }
    }

    @Override
    public List<T> update(Object shard, Update update, Criteria criteria) {
        JdbcExecuteContext<T, K> context = JdbcExecuteContext.<T, K>builder().metadata(this.metadata).update(update).criteria(criteria).build();
        int index = this.metadata.getShardCalculator().calculateDatasource(shard);
        int i = this.update(this.sources.get(index), context);
        if (i > 0) {
            return select(shard, criteria);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public T upsert(Object shard, T document) {
        T update = update(shard, document);
        if (update == null) {
            update = create(shard, document);
        }
        return update;
    }

    @Override
    public T delete(Object shard, T document) {
        K id = this.getId(document);
        T object = this.select(shard, id);
        JdbcExecuteContext<T, K> context = JdbcExecuteContext.<T, K>builder().metadata(this.metadata).document(document).build();
        this.dialect.resolveToDeleteCommand(context);
        int index = this.metadata.getShardCalculator().calculateDatasource(shard);
        int i = this.update(this.sources.get(index), context);
        if (i > 0) {
            return object;
        } else {
            return null;
        }
    }

    @Override
    public List<T> delete(Object shard, Criteria criteria) {
        List<T> result = select(shard, criteria);
        if (result.size() == 0) {
            return result;
        }
        JdbcExecuteContext<T, K> context = JdbcExecuteContext.<T, K>builder().metadata(this.metadata).criteria(criteria).build();
        this.dialect.resolveToDeleteCommand(context);
        int index = this.metadata.getShardCalculator().calculateDatasource(shard);
        int i = this.update(this.sources.get(index), context);
        if (i > 0) {
            return result;
        } else {
            return Collections.emptyList();
        }
    }
}

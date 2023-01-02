package org.tinger.jdbc.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.tinger.common.utils.CollectionUtils;
import org.tinger.jdbc.core.JdbcDriver;
import org.tinger.jdbc.dialect.JdbcExecuteContext;
import org.tinger.jdbc.queryable.Criteria;
import org.tinger.jdbc.queryable.Queryable;
import org.tinger.jdbc.queryable.Update;
import org.tinger.jdbc.source.TingerJdbcDataSource;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;

/**
 * Created by tinger on 2022-10-17
 */
public abstract class AbstractJdbcStaticsRepository<T, K> extends AbstractJdbcRepository<T, K> implements JdbcStaticsRepository<T, K> {
    private DataSource source;
    @Autowired
    private TingerJdbcDataSource tingerJdbcDataSource;

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        this.source = this.tingerJdbcDataSource.load(this.metadata.getDatasource());
    }

    @Override
    public T create(T document) {
        JdbcExecuteContext<T, K> context = JdbcExecuteContext.<T, K>builder().metadata(this.metadata).document(document).build();
        dialect.resolveToCreateCommand(context);
        int i = this.update(this.source, context);
        if (i > 0) {
            return select(getId(document));
        } else {
            return null;
        }
    }
    @Override
    public List<T> select() {
        JdbcExecuteContext<T, K> context = JdbcExecuteContext.<T, K>builder().metadata(this.metadata).build();
        this.dialect.resolveToSelectCommand(context);
        return this.select(this.source, context.getCommand());
    }

    @Override
    public T select(K id) {
        JdbcExecuteContext<T, K> context = JdbcExecuteContext.<T, K>builder().metadata(this.metadata).primary(id).build();
        dialect.resolveToSelectCommand(context);
        List<T> list = this.select(this.source, context);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    @Override
    public List<T> select(Criteria criteria) {
        JdbcExecuteContext<T, K> context = JdbcExecuteContext.<T, K>builder().metadata(this.metadata).criteria(criteria).build();
        this.dialect.resolveToSelectCommand(context);
        return this.select(source, context.getCommand(), context.getHandlers(), context.getParameters());
    }

    public List<T> select(Queryable queryable) {
        JdbcExecuteContext<T, K> context = JdbcExecuteContext.<T, K>builder()
                .metadata(this.metadata)
                .criteria(queryable.getCriteria()).ordered(queryable.getOrdered()).limited(queryable.getLimited())
                .build();
        this.dialect.resolveToSelectCommand(context);
        return this.select(source, context.getCommand(), context.getHandlers(), context.getParameters());
    }

    @Override
    public T update(T document) {
        JdbcExecuteContext<T, K> context = JdbcExecuteContext.<T, K>builder().metadata(this.metadata).document(document).build();
        dialect.resolveToUpdateCommand(context);
        int i = this.update(this.source, context.getCommand(), context.getHandlers(), context.getParameters());
        if (i > 0) {
            return select(getId(document));
        } else {
            return null;
        }
    }

    @Override
    public List<T> update(Update update, Criteria criteria) {
        JdbcExecuteContext<T, K> context = JdbcExecuteContext.<T, K>builder().metadata(this.metadata).update(update).criteria(criteria).build();
        int i = this.update(this.source, context);
        if (i > 0) {
            return select(criteria);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public T upsert(T document) {
        T update = update(document);
        if (update == null) {
            update = create(document);
        }
        return update;
    }

    @Override
    public T delete(T document) {
        K id = this.getId(document);
        T object = this.select(id);
        JdbcExecuteContext<T, K> context = JdbcExecuteContext.<T, K>builder().metadata(this.metadata).document(document).build();
        this.dialect.resolveToDeleteCommand(context);
        int i = this.update(this.source, context);
        if (i > 0) {
            return object;
        } else {
            return null;
        }
    }

    @Override
    public List<T> delete(Criteria criteria) {
        List<T> result = select(criteria);
        if (result.size() == 0) {
            return result;
        }
        JdbcExecuteContext<T, K> context = JdbcExecuteContext.<T, K>builder().metadata(this.metadata).criteria(criteria).build();
        this.dialect.resolveToDeleteCommand(context);
        int i = this.update(source, context);
        if (i > 0) {
            return result;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public JdbcDriver getDriver() {
        return JdbcDriver.MYSQL;
    }
}

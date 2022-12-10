package org.tinger.jdbc.mysql;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextAware;
import org.tinger.common.utils.CollectionUtils;
import org.tinger.jdbc.anno.JdbcDataSource;
import org.tinger.jdbc.anno.JdbcDataTableName;
import org.tinger.jdbc.anno.JdbcDatabaseName;
import org.tinger.jdbc.handler.JdbcHandler;
import org.tinger.jdbc.queryable.Criteria;
import org.tinger.jdbc.queryable.Update;
import org.tinger.jdbc.repository.Repository;
import org.tinger.jdbc.source.TingerJdbcDataSource;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by tinger on 2022-10-17
 */
public abstract class MysqlRepository<T, K> extends AbstractMysqlRepository<T, K> implements Repository<T, K>, InitializingBean, ApplicationContextAware {
    private DataSource source;
    private String table;
    private String database;

    @Autowired
    private TingerJdbcDataSource tingerJdbcDataSource;

    @Override
    public void afterPropertiesSet() {
        this.table = this.getClass().getAnnotation(JdbcDataTableName.class).value();
        this.database = this.getClass().getAnnotation(JdbcDatabaseName.class).value();
        String sourceName = this.getClass().getAnnotation(JdbcDataSource.class).value();
        this.source = this.tingerJdbcDataSource.load(sourceName);
    }

    @Override
    public T select(K id) {
        List<JdbcHandler<?>> handlers = new LinkedList<>();
        List<Object> parameters = new LinkedList<>();
        String command = buildSelectCommand(id, this.database, this.table, handlers, parameters);
        List<T> list = this.select(this.source, command, handlers, parameters);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    @Override
    public T create(T document) {
        List<JdbcHandler<?>> handlers = new LinkedList<>();
        List<Object> parameters = new LinkedList<>();
        String command = buildCreateCommand(document, this.database, this.table, handlers, parameters);
        int i = this.update(this.source, command, handlers, parameters);
        if (i > 0) {
            return select(getId(document));
        } else {
            return null;
        }
    }

    @Override
    public T update(T document) {
        List<JdbcHandler<?>> handlers = new LinkedList<>();
        List<Object> parameters = new LinkedList<>();
        String command = buildUpdateCommand(document, this.database, this.table, handlers, parameters);
        int i = this.update(this.source, command, handlers, parameters);
        if (i > 0) {
            return select(getId(document));
        } else {
            return null;
        }
    }

    @Override
    public List<T> update(Update update, Criteria criteria) {
        List<JdbcHandler<?>> handlers = new LinkedList<>();
        List<Object> parameters = new LinkedList<>();
        String command = buildUpdateCommand(update, criteria, database, table, handlers, parameters);
        int i = this.update(this.source, command, handlers, parameters);
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
        T object = this.select(getId(document));
        if (object == null) {
            return null;
        }
        List<JdbcHandler<?>> handlers = new LinkedList<>();
        List<Object> parameters = new LinkedList<>();
        String command = buildDeleteCommand(getId(document), this.database, this.table, handlers, parameters);
        int i = this.update(this.source, command, handlers, parameters);
        if (i > 0) {
            return object;
        } else {
            return null;
        }
    }

    @Override
    public List<T> delete(Criteria criteria) {
        List<JdbcHandler<?>> handlers = new LinkedList<>();
        List<Object> parameters = new LinkedList<>();
        List<T> result = select(criteria);
        if (result.size() == 0) {
            return result;
        }
        String deleteCommand = buildDeleteCommand(criteria, database, table, handlers, parameters);
        int i = this.update(source, deleteCommand, handlers, parameters);
        if (i > 0) {
            return result;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<T> select() {
        String command = buildSelectCommand(this.database, this.table);
        return this.select(this.source, command);
    }

    @Override
    public List<T> select(Criteria criteria) {
        List<JdbcHandler<?>> handlers = new LinkedList<>();
        List<Object> parameters = new LinkedList<>();
        String command = buildSelectCommand(criteria, database, table, handlers, parameters);
        return this.select(source, command, handlers, parameters);
    }
}

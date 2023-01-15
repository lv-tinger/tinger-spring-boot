package org.tinger.data.jdbc;

import org.tinger.data.core.anno.TingerDataSource;
import org.tinger.data.core.anno.TingerDataTable;
import org.tinger.data.core.anno.TingerDatabase;
import org.tinger.data.core.tsql.*;
import org.tinger.data.core.repo.TingerStaticsRepository;
import org.tinger.data.jdbc.context.*;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class JdbcStaticsRepository<T, K> extends TingerJdbcRepository<T, K> implements TingerStaticsRepository<T, K> {
    private final String datatableName;
    private final String databaseName;
    private final String datasourceName;

    private DataSource datasource;

    protected JdbcStaticsRepository() {
        super();

        Class<?> repositoryType = this.getClass();
        TingerDataTable tableAnnotation = repositoryType.getDeclaredAnnotation(TingerDataTable.class);
        TingerDataSource sourceAnnotation = repositoryType.getDeclaredAnnotation(TingerDataSource.class);
        TingerDatabase baseAnnotation = repositoryType.getDeclaredAnnotation(TingerDatabase.class);

        this.datatableName = tableAnnotation.value();
        this.datasourceName = sourceAnnotation.value();
        this.databaseName = baseAnnotation.value();
    }

    @Override
    public T create(T document) {
        try {
            JdbcExecuteContext context = new JdbcCreateExecuteContext(document, this.metadata, this.databaseName, this.datatableName).resolve();
            int count = this.update(this.datasource, context.getCommandText(), context.getParameterHandlers(), context.getParameterValues());
            return count > 0 ? document : null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<T> create(List<T> documents) {
        try {
            JdbcExecuteBatchContext context = new JdbcCreateExecuteBatchContext((List<Object>) documents, this.metadata, this.databaseName, this.datatableName).resolve();
            int count = this.updateBatch(this.datasource, context.getCommandText(), context.getParameterHandlers(), context.getParameterValues());
            return count > 0 ? documents : null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T update(T document) {
        try {
            JdbcExecuteContext context = new JdbcUpdateExecuteContext(document, this.metadata, this.databaseName, this.datatableName).resolve();
            int count = this.update(this.datasource, context.getCommandText(), context.getParameterHandlers(), context.getParameterValues());
            return count > 0 ? document : null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<T> update(List<T> documents) {
        try {
            JdbcExecuteBatchContext context = new JdbcUpdateExecuteBatchContext((List<Object>) documents, this.metadata, this.databaseName, this.datatableName).resolve();
            int count = this.updateBatch(this.datasource, context.getCommandText(), context.getParameterHandlers(), context.getParameterValues());
            return count > 0 ? documents : null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T upsert(T document) {
        T update = this.update(document);
        if (update == null) {
            update = this.create(document);
        }

        return update;
    }

    @Override
    public List<T> update(Update update, Criteria criteria) {
        return null;
    }

    @Override
    public T delete(K id) {
        return null;
    }

    @Override
    public List<T> delete(List<K> ids) {
        return null;
    }

    @Override
    public List<T> select() {
        return null;
    }

    @Override
    public T select(K id) {
        return null;
    }

    @Override
    public Map<K, T> select(Collection<K> ids) {
        return null;
    }

    @Override
    public List<T> select(Criteria criteria) {
        return null;
    }

    @Override
    public List<T> select(Criteria criteria, Ordered ordered, Limited limited) {
        return null;
    }
}

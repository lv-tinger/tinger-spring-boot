package org.tinger.data.jdbc;

import org.tinger.data.core.anno.TingerDataSource;
import org.tinger.data.core.anno.TingerDataTable;
import org.tinger.data.core.anno.TingerDatabase;
import org.tinger.data.core.repo.TingerStaticsRepository;
import org.tinger.data.core.tsql.Criteria;
import org.tinger.data.core.tsql.Limited;
import org.tinger.data.core.tsql.Ordered;
import org.tinger.data.core.tsql.Update;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class JdbcStaticsRepository<T, K> extends TingerJdbcRepository<T, K> implements TingerStaticsRepository<T, K> {
    private String datatableName;
    private String databaseName;
    private String datasourceName;

    private DataSource datasource;

    protected JdbcStaticsRepository() {
        super();
        resolveStorage();
    }

    private void resolveStorage() {
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
        return null;
    }

    @Override
    public List<T> create(List<T> documents) {
        return null;
    }

    @Override
    public T update(T document) {
        return null;
    }

    @Override
    public List<T> update(List<T> documents) {
        return null;
    }

    @Override
    public T upsert(T document) {
        return null;
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

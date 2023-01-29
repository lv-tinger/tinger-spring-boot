package org.tinger.data.jdbc;

import lombok.Getter;
import lombok.Setter;
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
import java.util.Collections;
import java.util.List;

@Getter
public class JdbcStaticsRepository<T, K> extends TingerJdbcRepository<T, K> implements TingerStaticsRepository<T, K> {
    private String datatableName;
    private String databaseName;
    private String datasourceName;

    @Getter
    @Setter
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
        try {
            JdbcExecuteContext<T, K> context = buildCreateContext(this.databaseName, this.datatableName).resolveParameters(document);
            int update = this.update(datasource, context);
            return update > 0 ? document : null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<T> create(List<T> documents) {
        try {
            JdbcExecuteContext<T, K> context = buildCreateContext(this.databaseName, this.datatableName).resolveParametersList(documents);
            int update = this.updateBatch(datasource, context);
            return update > 0 ? documents : Collections.emptyList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T update(T document) {
        try {
            JdbcExecuteContext<T, K> context = buildUpdateContext(this.databaseName, this.datatableName).resolveParameters(document);
            int update = this.update(datasource, context);
            return update > 0 ? document : null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<T> update(List<T> documents) {
        try {
            JdbcExecuteContext<T, K> context = buildUpdateContext(this.databaseName, this.datatableName).resolveParametersList(documents);
            int update = this.updateBatch(datasource, context);
            return update > 0 ? documents : Collections.emptyList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T upsert(T document) {
        return null;
    }

    @Override
    public List<T> update(Update update, Criteria criteria) {
        try {
            JdbcExecuteContext<T, K> context = buildUpdateContext(update, criteria, this.databaseName, this.datatableName);
            int i = this.update(datasource, context);
            return i > 0 ? this.select(criteria) : Collections.emptyList();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public T delete(K id) {
        try {
            T select = select(id);
            if (select == null) {
                return null;
            }
            JdbcExecuteContext<T, K> context = buildDeleteContext(this.databaseName, this.datatableName).resolveParameter(id);
            int update = this.update(datasource, context);
            return update > 0 ? select : null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<T> delete(List<K> ids) {
        try {
            List<T> select = select(ids);
            if (select.size() != ids.size()) {
                return Collections.emptyList();
            }
            JdbcExecuteContext<T, K> context = buildDeleteContext(this.databaseName, this.datatableName).resolveParameterList(ids);
            int update = this.update(datasource, context);
            return update > 0 ? select : null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<T> select() {
        String commandText = buildQueryCommandText(this.databaseName, this.datatableName);
        return this.select(datasource, commandText);
    }

    @Override
    public T select(K id) {
        try {
            JdbcExecuteContext<T, K> context = buildSelectContext(this.databaseName, this.datatableName).resolveParameter(id);
            List<T> list = this.select(datasource, context);
            return list.size() > 0 ? list.get(0) : null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<T> select(Collection<K> ids) {
        try {
            JdbcExecuteContext<T, K> context = buildSelectContext(this.databaseName, this.datatableName).resolveParameterList(ids.stream().toList());
            return this.selectBatch(datasource, context);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<T> select(Criteria criteria) {
        try {
            JdbcExecuteContext<T, K> context = buildQueryableContext(criteria, this.databaseName, this.datatableName);
            return this.select(datasource, context);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public List<T> select(Criteria criteria, Ordered ordered, Limited limited) {
        return null;
    }
}

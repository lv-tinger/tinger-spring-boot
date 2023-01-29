package org.tinger.data.jdbc;

import lombok.Getter;
import org.tinger.common.utils.ClassUtils;
import org.tinger.data.core.anno.TingerShardAlgorithm;
import org.tinger.data.core.repo.TingerDynamicRepository;
import org.tinger.data.core.tsql.Criteria;
import org.tinger.data.core.tsql.Limited;
import org.tinger.data.core.tsql.Ordered;
import org.tinger.data.core.tsql.Update;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class JdbcDynamicRepository<T, K, S> extends TingerJdbcRepository<T, K> implements TingerDynamicRepository<T, K, S> {

    @Getter
    private DynamicAlgorithm<S> algorithm;

    protected JdbcDynamicRepository() {
        super();
    }

    private void resolveStorage() {
        Class<?> repositoryType = this.getClass();
        TingerShardAlgorithm annotation = repositoryType.getDeclaredAnnotation(TingerShardAlgorithm.class);
        Class<?> algorithmType = ClassUtils.loadClass(annotation.value());
        algorithm = (DynamicAlgorithm<S>) ClassUtils.newInstance(algorithmType, new Class[]{Class.class}, new Object[]{repositoryType});
    }

    @Override
    public T create(S shard, T document) {
        try {
            DataSource datasource = algorithm.source(shard);
            String databaseName = algorithm.database(shard);
            String datatableName = algorithm.datatable(shard);
            JdbcExecuteContext<T, K> context = buildCreateContext(databaseName, datatableName).resolveParameters(document);
            int update = this.update(datasource, context);
            return update > 0 ? document : null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<T> create(S shard, List<T> documents) {
        try {
            DataSource datasource = algorithm.source(shard);
            String databaseName = algorithm.database(shard);
            String datatableName = algorithm.datatable(shard);
            JdbcExecuteContext<T, K> context = buildCreateContext(databaseName, datatableName).resolveParametersList(documents);
            int update = this.updateBatch(datasource, context);
            return update > 0 ? documents : Collections.emptyList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T update(S shard, T document) {
        try {
            DataSource datasource = algorithm.source(shard);
            String databaseName = algorithm.database(shard);
            String datatableName = algorithm.datatable(shard);
            JdbcExecuteContext<T, K> context = buildUpdateContext(databaseName, datatableName).resolveParameters(document);
            int update = this.update(datasource, context);
            return update > 0 ? document : null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<T> update(S shard, Update update, Criteria criteria) {
        try {
            DataSource datasource = algorithm.source(shard);
            String databaseName = algorithm.database(shard);
            String datatableName = algorithm.datatable(shard);
            JdbcExecuteContext<T, K> context = buildUpdateContext(update, criteria, databaseName, datatableName);
            int i = this.update(datasource, context);
            return i > 0 ? this.select(shard, criteria) : Collections.emptyList();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public List<T> update(S shard, List<T> documents) {
        try {
            DataSource datasource = algorithm.source(shard);
            String databaseName = algorithm.database(shard);
            String datatableName = algorithm.datatable(shard);
            JdbcExecuteContext<T, K> context = buildUpdateContext(databaseName, datatableName).resolveParametersList(documents);
            int update = this.updateBatch(datasource, context);
            return update > 0 ? documents : Collections.emptyList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T delete(S shard, K id) {
        try {
            DataSource datasource = algorithm.source(shard);
            String databaseName = algorithm.database(shard);
            String datatableName = algorithm.datatable(shard);

            T select = select(shard, id);
            if (select == null) {
                return null;
            }
            JdbcExecuteContext<T, K> context = buildDeleteContext(databaseName, datatableName).resolveParameter(id);
            int update = this.update(datasource, context);
            return update > 0 ? select : null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<T> delete(S shard, Criteria criteria) {
        return null;
    }

    @Override
    public List<T> delete(S shard, List<K> ids) {
        try {
            DataSource datasource = algorithm.source(shard);
            String databaseName = algorithm.database(shard);
            String datatableName = algorithm.datatable(shard);

            List<T> select = select(shard, ids);
            if (select.size() != ids.size()) {
                return Collections.emptyList();
            }
            JdbcExecuteContext<T, K> context = buildDeleteContext(databaseName, datatableName).resolveParameterList(ids);
            int update = this.update(datasource, context);
            return update > 0 ? select : null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T select(S shard, K id) {
        try {
            DataSource datasource = algorithm.source(shard);
            String databaseName = algorithm.database(shard);
            String datatableName = algorithm.datatable(shard);
            JdbcExecuteContext<T, K> context = buildSelectContext(databaseName, datatableName).resolveParameter(id);
            List<T> list = this.select(datasource, context);
            return list.size() > 0 ? list.get(0) : null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<T> select(S shard, Collection<K> ids) {
        try {
            DataSource datasource = algorithm.source(shard);
            String databaseName = algorithm.database(shard);
            String datatableName = algorithm.datatable(shard);
            JdbcExecuteContext<T, K> context = buildSelectContext(databaseName, datatableName).resolveParameterList(ids.stream().toList());
            return this.selectBatch(datasource, context);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<T> select(S shard, Criteria criteria) {
        try {
            DataSource datasource = algorithm.source(shard);
            String databaseName = algorithm.database(shard);
            String datatableName = algorithm.datatable(shard);
            JdbcExecuteContext<T, K> context = buildQueryableContext(criteria, databaseName, datatableName);
            return this.select(datasource, context);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public List<T> select(S shard, Criteria criteria, Ordered ordered, Limited limited) {
        return null;
    }
}

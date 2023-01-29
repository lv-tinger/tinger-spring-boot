package org.tinger.data.jdbc;

import lombok.Getter;
import lombok.Setter;
import org.tinger.data.core.anno.TingerDataSource;
import org.tinger.data.core.anno.TingerDataTable;
import org.tinger.data.core.anno.TingerDatabase;

import javax.sql.DataSource;
import java.util.List;

@Getter
public abstract class DynamicAlgorithm<S> {

    private String databaseName;
    private String datatableName;
    private String datasourceName;
    private boolean databaseShard;
    private boolean datatableShard;
    private boolean datasourceShard;

    @Getter
    @Setter
    private List<DataSource> dataSources;

    protected DynamicAlgorithm(Class<?> repositoryType) {
        TingerDataTable tableAnnotation = repositoryType.getDeclaredAnnotation(TingerDataTable.class);
        TingerDataSource sourceAnnotation = repositoryType.getDeclaredAnnotation(TingerDataSource.class);
        TingerDatabase baseAnnotation = repositoryType.getDeclaredAnnotation(TingerDatabase.class);

        this.datatableName = tableAnnotation.value();
        this.datatableShard = tableAnnotation.shard();
        this.datasourceName = sourceAnnotation.value();
        this.datasourceShard = sourceAnnotation.shard();
        this.databaseName = baseAnnotation.value();
        this.databaseShard = baseAnnotation.shard();
    }

    public abstract DataSource source(S object);

    public abstract String datatable(S object);

    public abstract String database(S object);
}

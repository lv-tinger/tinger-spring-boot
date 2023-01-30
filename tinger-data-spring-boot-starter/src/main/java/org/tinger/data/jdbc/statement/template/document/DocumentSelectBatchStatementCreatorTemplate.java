package org.tinger.data.jdbc.statement.template.document;

import org.tinger.common.buffer.TingerMapBuffer;
import org.tinger.common.utils.StringUtils;
import org.tinger.data.core.meta.TingerMetadata;
import org.tinger.data.core.meta.TingerProperty;
import org.tinger.data.jdbc.handler.JdbcHandler;
import org.tinger.data.jdbc.handler.JdbcHandlerHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by tinger on 2023-01-30
 */
public class DocumentSelectBatchStatementCreatorTemplate {
    private static final int SINGLE_BATCH = 1;
    private static final int SMALL_BATCH = 4;
    private static final int MEDIUM_BATCH = 11;
    private static final int LARGE_BATCH = 51;

    private final List<Integer> BATCH_SIZE = Arrays.asList(LARGE_BATCH, MEDIUM_BATCH, SMALL_BATCH, SINGLE_BATCH);

    private final Map<Integer, String> BATCH_SQL = new HashMap<>();

    protected JdbcHandler<?> handler;
    protected TingerProperty property;

    protected final TingerMetadata<?> metadata;

    public DocumentSelectBatchStatementCreatorTemplate(TingerMetadata<?> metadata) {
        this.metadata = metadata;
    }

    private DocumentSelectBatchStatementCreatorTemplate generate() {
        this.property = metadata.getPrimaryKey();
        this.handler = JdbcHandlerHolder.getInstance().get(this.property.getType());
        List<String> names = new LinkedList<>();
        names.add("`" + this.property.getColumn() + "`");
        metadata.getProperties().forEach(x -> names.add("`" + x.getColumn() + "`"));
        String columnNames = StringUtils.join(names, ", ");
        String commandText = "SELECT " + columnNames + " FROM `[]`.`[]` WHERE `" + this.property.getColumn() + "` in ";
        for (int size : BATCH_SIZE) {
            String params = StringUtils.repeat("?", ", ", size);
            BATCH_SQL.put(size, commandText + "(" + params + ")");
        }
        return this;
    }

    public List<PreparedStatement> statement(Connection connection, String database, String datatable, List<?> parameters) throws SQLException {
        List<PreparedStatement> statements = new LinkedList<>();
        int count = parameters.size();
        for (int size : BATCH_SIZE) {
            while (count >= size) {
                String sql = StringUtils.format(BATCH_SQL.get(size), database, datatable);
                PreparedStatement statement = connection.prepareStatement(sql);
                int skip = parameters.size() - count;
                for (int i = 0; i < size; i++) {
                    handler.setParameter(statement, i + 1, parameters.get(skip + i));
                }
                statements.add(statement);
                count = count - size;
            }
        }
        return statements;
    }

    private static final TingerMapBuffer<Class<?>, DocumentSelectBatchStatementCreatorTemplate> BUFFER = new TingerMapBuffer<>();

    public static DocumentSelectBatchStatementCreatorTemplate build(TingerMetadata<?> metadata) {
        return BUFFER.get(metadata.getType(), () -> new DocumentSelectBatchStatementCreatorTemplate(metadata).generate());
    }
}

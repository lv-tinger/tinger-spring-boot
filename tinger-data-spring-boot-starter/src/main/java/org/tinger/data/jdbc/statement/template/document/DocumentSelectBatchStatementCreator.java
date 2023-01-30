package org.tinger.data.jdbc.statement.template.document;

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
public class DocumentSelectBatchStatementCreator<T, K> {
    private static final int SINGLE_BATCH = 1;
    private static final int SMALL_BATCH = 4;
    private static final int MEDIUM_BATCH = 11;
    private static final int LARGE_BATCH = 51;

    private final List<Integer> BATCH_SIZE = Arrays.asList(SINGLE_BATCH, SMALL_BATCH, MEDIUM_BATCH, LARGE_BATCH);

    private final Map<Integer, String> BATCH_SQL = new HashMap<>();

    protected JdbcHandler<?> handler;
    protected TingerProperty property;

    protected final TingerMetadata<T> metadata;

    public DocumentSelectBatchStatementCreator(TingerMetadata<T> metadata) {
        this.metadata = metadata;
    }

    private DocumentSelectBatchStatementCreator generate() {
        this.property = metadata.getPrimaryKey();
        this.handler = JdbcHandlerHolder.getInstance().get(this.property.getType());
        List<String> names = new LinkedList<>();
        names.add("`" + this.property.getColumn() + "`");
        metadata.getProperties().forEach(x -> names.add("`" + x.getColumn() + "`"));
        String columnNames = StringUtils.join(names, ", ");
        String commandText = "SELECT " + columnNames + " FROM `[]`.`[]` WHERE `" + this.property.getColumn() + "` in ([])";
        for (int size : BATCH_SIZE) {
            buildSql(commandText, size);
        }
        return this;
    }

    private void buildSql(String template, int size) {
        String params = StringUtils.repeat("?", ", ", size);
        String sql = StringUtils.format(template, params);
        BATCH_SQL.put(size, sql);
    }

    public List<PreparedStatement> prepared(Connection connection, List<K> parameters) {
        List<PreparedStatement> statements = new LinkedList<>();
        int count = parameters.size();
        
        return statements;
    }

    private PreparedStatement prepared(Connection connection, List<K> parameters, int skip, int count) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(BATCH_SQL.get(count));
        for (int i = 0; i < count; i++) {
            handler.setParameter(statement, i + 1, parameters.get(skip + i));
        }
        return statement;
    }

}

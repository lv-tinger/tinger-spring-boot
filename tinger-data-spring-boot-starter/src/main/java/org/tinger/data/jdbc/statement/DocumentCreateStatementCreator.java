package org.tinger.data.jdbc.statement;

import org.tinger.common.buffer.TingerMapBuffer;
import org.tinger.common.utils.StringUtils;
import org.tinger.data.core.meta.TingerMetadata;
import org.tinger.data.core.meta.TingerProperty;
import org.tinger.data.jdbc.handler.JdbcHandlerHolder;

import java.util.ArrayList;
import java.util.List;

public class DocumentCreateStatementCreator extends AbstractDocumentStatementCreator {
    private static final TingerMapBuffer<Class<?>, StatementCreator> BUFFER = new TingerMapBuffer<>();

    private DocumentCreateStatementCreator(TingerMetadata<?> metadata) {
        super(metadata);
    }

    private DocumentCreateStatementCreator generate() {
        JdbcHandlerHolder jdbcHandlerHolder = JdbcHandlerHolder.getInstance();
        int size = metadata.getProperties().size();
        this.properties = new ArrayList<>(size + 1);
        this.handlers = new ArrayList<>(size + 1);

        TingerProperty primaryKey = metadata.getPrimaryKey();
        properties.add(primaryKey);
        handlers.add(jdbcHandlerHolder.get(primaryKey.getType()));

        List<TingerProperty> ps = metadata.getProperties();
        for (TingerProperty p : ps) {
            properties.add(p);
            handlers.add(jdbcHandlerHolder.get(p.getType()));
        }

        String columnNames = StringUtils.join(properties.stream().map(x -> "`" + x.getColumn() + "`").toList(), ", ");
        String parameters = StringUtils.repeat("?", ", ", properties.size());

        this.commandText = "INSERT INTO `[]`.`[]`(" + columnNames + ") VALUES(" + parameters + ")";
        return this;
    }

    public static StatementCreator build(TingerMetadata<?> metadata) {
        return BUFFER.get(metadata.getType(), () -> new DocumentCreateStatementCreator(metadata).generate());
    }

    public static void register(Class<?> type, StatementCreator creator) {
        BUFFER.set(type, creator);
    }
}
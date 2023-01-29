package org.tinger.data.jdbc.statement;

import org.tinger.common.buffer.TingerMapBuffer;
import org.tinger.common.utils.StringUtils;
import org.tinger.data.core.meta.TingerMetadata;
import org.tinger.data.core.meta.TingerProperty;
import org.tinger.data.jdbc.handler.JdbcHandlerHolder;

import java.util.ArrayList;
import java.util.List;

public class DocumentUpdateStatementCreator extends AbstractDocumentStatementCreator {
    private static final TingerMapBuffer<Class<?>, StatementCreator> BUFFER = new TingerMapBuffer<>();

    private DocumentUpdateStatementCreator(TingerMetadata<?> metadata) {
        super(metadata);
    }

    private DocumentUpdateStatementCreator generate() {
        JdbcHandlerHolder jdbcHandlerHolder = JdbcHandlerHolder.getInstance();
        int size = metadata.getProperties().size();
        this.properties = new ArrayList<>(size + 1);
        this.handlers = new ArrayList<>(size + 1);

        List<TingerProperty> ps = metadata.getProperties();
        for (TingerProperty p : ps) {
            properties.add(p);
            this.handlers.add(jdbcHandlerHolder.get(p.getType()));
        }

        String columnNames = StringUtils.join(metadata.getProperties().stream().map(x -> "`" + x.getColumn() + "` = ?").toList(), ", ");

        TingerProperty primaryKey = metadata.getPrimaryKey();
        properties.add(primaryKey);
        this.handlers.add(jdbcHandlerHolder.get(primaryKey.getType()));
        this.commandText = "UPDATE `[]`.`[]` SET " + columnNames + " WHERE `" + primaryKey.getColumn() + "` = ?";

        return this;
    }

    public static StatementCreator build(TingerMetadata<?> metadata) {
        return BUFFER.get(metadata.getType(), () -> new DocumentUpdateStatementCreator(metadata).generate());
    }

    public static void register(Class<?> type, StatementCreator creator) {
        BUFFER.set(type, creator);
    }
}
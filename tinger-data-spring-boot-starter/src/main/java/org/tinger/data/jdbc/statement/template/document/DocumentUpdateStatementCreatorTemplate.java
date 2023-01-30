package org.tinger.data.jdbc.statement.template.document;

import org.tinger.common.buffer.TingerMapBuffer;
import org.tinger.common.utils.StringUtils;
import org.tinger.data.core.meta.TingerMetadata;
import org.tinger.data.core.meta.TingerProperty;
import org.tinger.data.jdbc.handler.JdbcHandlerHolder;
import org.tinger.data.jdbc.statement.template.StatementCreatorTemplate;

import java.util.ArrayList;
import java.util.List;

public class DocumentUpdateStatementCreatorTemplate extends AbstractDocumentStatementCreatorTemplate {
    private static final TingerMapBuffer<Class<?>, StatementCreatorTemplate> BUFFER = new TingerMapBuffer<>();

    private DocumentUpdateStatementCreatorTemplate(TingerMetadata<?> metadata) {
        super(metadata);
    }

    private DocumentUpdateStatementCreatorTemplate generate() {
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

    public static StatementCreatorTemplate build(TingerMetadata<?> metadata) {
        return BUFFER.get(metadata.getType(), () -> new DocumentUpdateStatementCreatorTemplate(metadata).generate());
    }

    public static void register(Class<?> type, StatementCreatorTemplate creator) {
        BUFFER.set(type, creator);
    }
}
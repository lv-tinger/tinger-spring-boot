package org.tinger.data.jdbc.statement.template.document;

import org.tinger.common.buffer.TingerMapBuffer;
import org.tinger.common.utils.StringUtils;
import org.tinger.data.core.meta.TingerMetadata;
import org.tinger.data.core.meta.TingerProperty;
import org.tinger.data.jdbc.handler.JdbcHandlerHolder;
import org.tinger.data.jdbc.statement.template.StatementCreatorTemplate;

import java.util.ArrayList;
import java.util.List;

public class DocumentCreateStatementCreatorTemplate extends AbstractDocumentStatementCreatorTemplate {
    private static final TingerMapBuffer<Class<?>, StatementCreatorTemplate> BUFFER = new TingerMapBuffer<>();

    private DocumentCreateStatementCreatorTemplate generate(TingerMetadata<?> metadata) {
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

    public static StatementCreatorTemplate build(TingerMetadata<?> metadata) {
        return BUFFER.get(metadata.getType(), () -> new DocumentCreateStatementCreatorTemplate().generate(metadata));
    }

    public static void register(Class<?> type, StatementCreatorTemplate creator) {
        BUFFER.set(type, creator);
    }
}
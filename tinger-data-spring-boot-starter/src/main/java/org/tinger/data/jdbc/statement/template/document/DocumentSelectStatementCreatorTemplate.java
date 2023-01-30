package org.tinger.data.jdbc.statement.template.document;

import org.tinger.common.buffer.TingerMapBuffer;
import org.tinger.common.utils.StringUtils;
import org.tinger.data.core.meta.TingerMetadata;
import org.tinger.data.core.meta.TingerProperty;
import org.tinger.data.jdbc.handler.JdbcHandlerHolder;

import java.util.LinkedList;
import java.util.List;

public class DocumentSelectStatementCreatorTemplate extends AbstractPrimaryKeyStatementCreatorTemplate {
    private static final TingerMapBuffer<Class<?>, DocumentSelectStatementCreatorTemplate> BUFFER = new TingerMapBuffer<>();

    private DocumentSelectStatementCreatorTemplate(TingerMetadata<?> metadata) {
        super(metadata);
    }

    private DocumentSelectStatementCreatorTemplate generate() {
        TingerProperty primaryKey = metadata.getPrimaryKey();
        this.property = primaryKey;
        this.handler = JdbcHandlerHolder.getInstance().get(primaryKey.getType());
        List<String> names = new LinkedList<>();
        names.add("`" + primaryKey.getColumn() + "`");
        metadata.getProperties().forEach(x -> names.add("`" + x.getColumn() + "`"));
        String columnNames = StringUtils.join(names, ", ");
        this.commandText = "SELECT " + columnNames + " FROM `[]`.`[]` WHERE `" + primaryKey.getColumn() + "` = ?";
        return this;
    }

    public static DocumentSelectStatementCreatorTemplate build(TingerMetadata<?> metadata) {
        return BUFFER.get(metadata.getType(), () -> new DocumentSelectStatementCreatorTemplate(metadata).generate());
    }
}
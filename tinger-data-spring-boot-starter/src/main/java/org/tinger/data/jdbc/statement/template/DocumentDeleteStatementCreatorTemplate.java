package org.tinger.data.jdbc.statement.template;

import org.tinger.common.buffer.TingerMapBuffer;
import org.tinger.data.core.meta.TingerMetadata;
import org.tinger.data.jdbc.handler.JdbcHandlerHolder;

public class DocumentDeleteStatementCreatorTemplate extends AbstractPrimaryKeyStatementCreatorTemplate {
    private static final TingerMapBuffer<Class<?>, DocumentDeleteStatementCreatorTemplate> BUFFER = new TingerMapBuffer<>();

    private DocumentDeleteStatementCreatorTemplate(TingerMetadata<?> metadata) {
        super(metadata);
    }

    private DocumentDeleteStatementCreatorTemplate generate() {
        this.property = metadata.getPrimaryKey();
        this.handler = JdbcHandlerHolder.getInstance().get(this.property.getType());
        this.commandText = "DELETE FROM `[]`.`[]` WHERE `" + this.property.getColumn() + "` = ?";
        return this;
    }

    public static DocumentDeleteStatementCreatorTemplate build(TingerMetadata<?> metadata) {
        return BUFFER.get(metadata.getType(), () -> new DocumentDeleteStatementCreatorTemplate(metadata).generate());
    }
}
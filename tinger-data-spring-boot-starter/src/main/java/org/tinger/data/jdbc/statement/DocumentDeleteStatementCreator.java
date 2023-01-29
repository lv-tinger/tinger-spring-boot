package org.tinger.data.jdbc.statement;

import org.tinger.common.buffer.TingerMapBuffer;
import org.tinger.data.core.meta.TingerMetadata;
import org.tinger.data.jdbc.handler.JdbcHandlerHolder;

public class DocumentDeleteStatementCreator extends AbstractPrimaryKeyStatementCreator {
    private static final TingerMapBuffer<Class<?>, DocumentDeleteStatementCreator> BUFFER = new TingerMapBuffer<>();

    private DocumentDeleteStatementCreator(TingerMetadata<?> metadata) {
        super(metadata);
    }

    private DocumentDeleteStatementCreator generate() {
        this.property = metadata.getPrimaryKey();
        this.handler = JdbcHandlerHolder.getInstance().get(this.property.getType());
        this.commandText = "DELETE FROM `[]`.`[]` WHERE `" + this.property.getColumn() + "` = ?";
        return this;
    }

    public static DocumentDeleteStatementCreator build(TingerMetadata<?> metadata) {
        return BUFFER.get(metadata.getType(), () -> new DocumentDeleteStatementCreator(metadata).generate());
    }
}
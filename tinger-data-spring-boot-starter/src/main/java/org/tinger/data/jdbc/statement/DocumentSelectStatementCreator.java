package org.tinger.data.jdbc.statement;

import org.tinger.common.buffer.TingerMapBuffer;
import org.tinger.common.utils.StringUtils;
import org.tinger.data.core.meta.TingerMetadata;
import org.tinger.data.core.meta.TingerProperty;
import org.tinger.data.jdbc.handler.JdbcHandlerHolder;

import java.util.LinkedList;
import java.util.List;

public class DocumentSelectStatementCreator extends AbstractPrimaryKeyStatementCreator {
    private static final TingerMapBuffer<Class<?>, DocumentSelectStatementCreator> BUFFER = new TingerMapBuffer<>();

    private DocumentSelectStatementCreator(TingerMetadata<?> metadata) {
        super(metadata);
    }

    private DocumentSelectStatementCreator generate() {
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

    public static DocumentSelectStatementCreator build(TingerMetadata<?> metadata) {
        return BUFFER.get(metadata.getType(), () -> new DocumentSelectStatementCreator(metadata).generate());
    }
}

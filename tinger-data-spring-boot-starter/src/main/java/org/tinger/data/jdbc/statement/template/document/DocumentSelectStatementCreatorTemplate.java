package org.tinger.data.jdbc.statement.template.document;

import org.tinger.common.buffer.TingerMapBuffer;
import org.tinger.common.utils.StringUtils;
import org.tinger.data.core.meta.TingerMetadata;
import org.tinger.data.core.meta.TingerProperty;
import org.tinger.data.jdbc.handler.JdbcHandlerHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.LinkedList;
import java.util.List;

public class DocumentSelectStatementCreatorTemplate extends AbstractPrimaryKeyStatementCreatorTemplate {
    private static final TingerMapBuffer<Class<?>, DocumentSelectStatementCreatorTemplate> BUFFER = new TingerMapBuffer<>();

    private DocumentSelectStatementCreatorTemplate generate(TingerMetadata<?> metadata) {
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

    @Override
    public PreparedStatement statement(Connection connection, String database, String datatable, Object parameter) throws Exception {
        String command = StringUtils.format(this.commandText, database, datatable);
        PreparedStatement statement = connection.prepareStatement(command);
        one(statement, parameter);
        return statement;
    }

    public static DocumentSelectStatementCreatorTemplate build(TingerMetadata<?> metadata) {
        return BUFFER.get(metadata.getType(), () -> new DocumentSelectStatementCreatorTemplate().generate(metadata));
    }
}
package org.tinger.data.jdbc.statement.template.document;

import org.tinger.common.utils.StringUtils;
import org.tinger.data.core.meta.TingerMetadata;
import org.tinger.data.jdbc.statement.template.StatementCreatorTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public abstract class AbstractStatementCreatorTemplate implements StatementCreatorTemplate {
    protected String commandText;

    @Override
    public PreparedStatement statement(Connection connection, String database, String datatable, Object parameter) throws Exception {
        String command = StringUtils.format(this.commandText, database, datatable);
        PreparedStatement statement = connection.prepareStatement(command);
        if (parameter instanceof List<?>) {
            many(statement, (List<?>) parameter);
        } else {
            one(statement, parameter);
        }
        return statement;
    }

    protected abstract void one(PreparedStatement statement, Object parameter) throws Exception;

    protected abstract void many(PreparedStatement statement, List<?> parameters) throws Exception;
}
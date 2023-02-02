package org.tinger.data.jdbc.statement.template.document;

import org.tinger.data.core.meta.TingerProperty;
import org.tinger.data.jdbc.handler.JdbcHandler;

import java.sql.PreparedStatement;
import java.util.List;

public abstract class AbstractPrimaryKeyStatementCreatorTemplate extends AbstractStatementCreatorTemplate {

    protected JdbcHandler<?> handler;
    protected TingerProperty property;

    @Override
    protected void one(PreparedStatement statement, Object parameter) throws Exception {
        this.handler.setParameter(statement, 1, parameter);
    }

    @Override
    protected void many(PreparedStatement statement, List<?> parameters) throws Exception {
        for (Object parameter : parameters) {
            this.handler.setParameter(statement, 1, parameter);
            statement.addBatch();
        }
    }
}

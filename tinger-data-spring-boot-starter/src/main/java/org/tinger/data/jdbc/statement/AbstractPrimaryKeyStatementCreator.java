package org.tinger.data.jdbc.statement;

import org.tinger.data.core.meta.TingerMetadata;
import org.tinger.data.core.meta.TingerProperty;
import org.tinger.data.jdbc.handler.JdbcHandler;

import java.sql.PreparedStatement;
import java.util.List;

public abstract class AbstractPrimaryKeyStatementCreator extends AbstractStatementCreator {

    protected JdbcHandler<?> handler;
    protected TingerProperty property;

    protected AbstractPrimaryKeyStatementCreator(TingerMetadata<?> metadata) {
        super(metadata);
    }

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

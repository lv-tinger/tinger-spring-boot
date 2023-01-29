package org.tinger.data.jdbc.statement;

import org.tinger.data.core.meta.TingerMetadata;
import org.tinger.data.core.meta.TingerProperty;
import org.tinger.data.jdbc.handler.JdbcHandler;

import java.sql.PreparedStatement;
import java.util.List;

public abstract class AbstractDocumentStatementCreator extends AbstractStatementCreator {
    protected List<JdbcHandler<?>> handlers;
    protected List<TingerProperty> properties;

    protected AbstractDocumentStatementCreator(TingerMetadata<?> metadata) {
        super(metadata);
    }

    @Override
    protected void one(PreparedStatement statement, Object parameter) throws Exception {
        int size = this.properties.size();
        for (int i = 0; i < size; i++) {
            this.handlers.get(i).setParameter(statement, i + 1, this.properties.get(i).getValue(parameter));
        }
    }

    @Override
    protected void many(PreparedStatement statement, List<?> parameters) throws Exception {
        int size = this.properties.size();
        for (Object parameter : parameters) {
            for (int i = 0; i < size; i++) {
                this.handlers.get(i).setParameter(statement, i + 1, this.properties.get(i).getValue(parameter));
            }
            statement.addBatch();
        }
    }
}

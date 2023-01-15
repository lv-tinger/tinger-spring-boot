package org.tinger.data.jdbc.context;

import org.tinger.data.core.meta.TingerMetadata;
import org.tinger.data.jdbc.handler.JdbcHandler;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractJdbcExecuteBatchContext implements JdbcExecuteBatchContext {
    protected List<Object> documents;
    protected TingerMetadata<?> metadata;
    protected String datatable;

    protected String database;
    private String commandText;
    private List<JdbcHandler<?>> parameterHandlers = new LinkedList<>();
    private List<List<Object>> parameterValues = new LinkedList<>();

    @Override
    public String getCommandText() {
        return this.commandText;
    }

    @Override
    public List<JdbcHandler<?>> getParameterHandlers() {
        return this.parameterHandlers;
    }

    @Override
    public List<List<Object>> getParameterValues() {
        return this.parameterValues;
    }

    protected void setCommandText(String commandText) {
        this.commandText = commandText;
    }

    protected void setParameterHandlers(List<JdbcHandler<?>> parameterHandlers) {
        this.parameterHandlers = parameterHandlers;
    }

    protected void setParameterValues(List<List<Object>> parameterValues) {
        this.parameterValues = parameterValues;
    }

    protected AbstractJdbcExecuteBatchContext(List<Object> documents, TingerMetadata<?> metadata, String database, String datatable) {
        this.documents = documents;
        this.metadata = metadata;
        this.database = database;
        this.datatable = datatable;
    }
}

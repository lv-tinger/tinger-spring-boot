package org.tinger.data.jdbc.context;

import org.tinger.data.jdbc.handler.JdbcHandler;

import java.util.List;

public interface JdbcExecuteBatchContext {
    String getCommandText();

    List<JdbcHandler<?>> getParameterHandlers();

    List<List<Object>> getParameterValues();

    JdbcExecuteBatchContext resolve() throws Exception;
}

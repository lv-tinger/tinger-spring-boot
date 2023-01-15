package org.tinger.data.jdbc.context;

import org.tinger.data.jdbc.handler.JdbcHandler;

import java.util.List;

public interface JdbcExecuteContext {
    String getCommandText();

    List<JdbcHandler<?>> getParameterHandlers();

    List<Object> getParameterValues();

    JdbcExecuteContext resolve() throws Exception;
}
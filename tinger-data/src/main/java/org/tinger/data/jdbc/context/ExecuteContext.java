package org.tinger.data.jdbc.context;

import lombok.Getter;
import lombok.Setter;
import org.tinger.data.core.meta.TingerProperty;
import org.tinger.data.jdbc.handler.JdbcHandler;

import java.util.List;

/**
 * Created by tinger on 2023-01-17
 */
@Getter
@Setter
public class ExecuteContext {
    private String commandText;
    private List<TingerProperty> properties;
    private List<JdbcHandler<?>> handlers;
}
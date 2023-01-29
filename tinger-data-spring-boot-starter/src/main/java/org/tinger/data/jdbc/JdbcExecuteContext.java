package org.tinger.data.jdbc;

import lombok.Builder;
import lombok.Getter;
import org.tinger.data.core.meta.TingerProperty;
import org.tinger.data.jdbc.handler.JdbcHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Getter
@Builder
public class JdbcExecuteContext<T, K> {
    private List<TingerProperty> properties;
    private List<JdbcHandler<?>> handlers;
    private String commandText;
    private List<Object> parameters;
    private List<List<Object>> parametersList;

    public JdbcExecuteContext<T, K> resolveParameters(T object) throws Exception {
        parameters = new ArrayList<>();
        for (TingerProperty x : properties) {
            parameters.add(x.getValue(object));
        }
        return this;
    }

    public JdbcExecuteContext<T, K> resolveParametersList(List<T> objects) throws Exception {
        parametersList = new ArrayList<>();
        for (Object object : objects) {
            List<Object> parameter = new LinkedList<>();
            for (TingerProperty x : properties) {
                parameter.add(x.getValue(object));
            }
            parametersList.add(parameter);
        }
        return this;
    }

    public JdbcExecuteContext<T, K> resolveParameter(K object) {
        this.parameters = Collections.singletonList(object);
        return this;
    }

    public JdbcExecuteContext<T, K> resolveParameterList(List<K> object) {
        this.parameters = new ArrayList<>(object);
        return this;
    }
}

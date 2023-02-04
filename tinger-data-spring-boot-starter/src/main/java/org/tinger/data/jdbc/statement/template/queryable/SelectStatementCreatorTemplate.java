package org.tinger.data.jdbc.statement.template.queryable;

import org.tinger.common.buffer.TingerMapBuffer;
import org.tinger.common.utils.StringUtils;
import org.tinger.data.core.tsql.Criteria;
import org.tinger.data.jdbc.handler.JdbcHandler;
import org.tinger.data.jdbc.statement.template.StatementCreatorTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

/**
 * Created by tinger on 2023-02-02
 */
public class SelectStatementCreatorTemplate implements StatementCreatorTemplate {
    private String commandText;
    private int parameterLength;
    private List<JdbcHandler<?>> parameterHandlers;
    private List<Integer> parameterSequences;

    private SelectStatementCreatorTemplate generate(Queryable queryable) {
        Criteria criteria = queryable.where();

        return this;
    }

    @Override
    public PreparedStatement statement(Connection connection, String database, String datatable, Object parameter) throws Exception {
        String command = StringUtils.format(commandText, database, datatable);
        PreparedStatement statement = connection.prepareStatement(command);
        if (parameter instanceof List<?>) {
            List<?> ps = (List<?>) parameter;
            for (int i = 0; i < parameterLength; i++) {
                Object value = ps.get(parameterSequences.get(i));
                parameterHandlers.get(0).setParameter(statement, i + 1, value);
            }
        } else if (parameter.getClass().isArray()) {
            Object[] ps = (Object[]) parameter;
            for (int i = 0; i < parameterLength; i++) {
                Object value = ps[parameterSequences.get(i)];
                parameterHandlers.get(0).setParameter(statement, i + 1, value);
            }
        } else {
            if (parameterLength > 1) {
                throw new RuntimeException();
            }
            parameterHandlers.get(0).setParameter(statement, 1, parameter);
        }
        return statement;
    }

    private static final TingerMapBuffer<String, SelectStatementCreatorTemplate> BUFFER = new TingerMapBuffer<>();

    public static SelectStatementCreatorTemplate build(String name, Queryable queryable) {
        String named = queryable.metadata().getType().getSimpleName() + "_" + name;
        return BUFFER.get(named, () -> new SelectStatementCreatorTemplate().generate(queryable));
    }
}

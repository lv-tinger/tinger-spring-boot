package org.tinger.data.jdbc.statement.template.queryable;

import org.tinger.common.buffer.TingerMapBuffer;
import org.tinger.common.utils.StringUtils;
import org.tinger.data.core.tsql.Queryable;
import org.tinger.data.core.tsql.Updatable;
import org.tinger.data.jdbc.handler.JdbcHandler;
import org.tinger.data.jdbc.statement.template.StatementCreatorTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

/**
 * Created by tinger on 2023-02-02
 */
public class UpdateStatementCreatorTemplate implements StatementCreatorTemplate {
    private String commandText;
    private int parameterLength;
    private List<JdbcHandler<?>> parameterHandlers;
    private List<Integer> parameterSequences;

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

    private UpdateStatementCreatorTemplate generate(Updatable updatable) {
        MysqlTransfer transfer = MysqlTransfer.builder().metadata(updatable.update()).update(updatable.set()).criteria(updatable.where()).build();
        this.commandText = "UPDATE `[]`.`[]` SET " + transfer.getUpdateExpression();
        if (StringUtils.isNoneEmpty(transfer.getWhereExpression())) {
            commandText += " WHERE " + transfer.getWhereExpression();
        }
        this.parameterHandlers = transfer.getJdbcHandlers();
        this.parameterSequences = transfer.getParameters();
        this.parameterLength = this.parameterHandlers.size();
        return this;
    }


    private static final TingerMapBuffer<String, UpdateStatementCreatorTemplate> BUFFER = new TingerMapBuffer<>();

    public static UpdateStatementCreatorTemplate build(Updatable updatable) {
        String named = updatable.update().getType().getSimpleName() + "_" + updatable.name();
        return BUFFER.get(named, () -> new UpdateStatementCreatorTemplate().generate(updatable));
    }
}

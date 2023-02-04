package org.tinger.data.jdbc.statement.template.queryable;

import org.tinger.common.buffer.TingerMapBuffer;
import org.tinger.common.utils.StringUtils;
import org.tinger.data.core.meta.TingerMetadata;
import org.tinger.data.core.tsql.Queryable;
import org.tinger.data.jdbc.handler.JdbcHandler;
import org.tinger.data.jdbc.statement.template.StatementCreatorTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.LinkedList;
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
        MysqlTransfer transfer = MysqlTransfer.builder().metadata(queryable.metadata()).criteria(queryable.where()).ordered(queryable.order()).limited(queryable.limit()).build().resolve();
        commandText = selectBody(queryable.metadata());
        if (StringUtils.isNoneEmpty(transfer.getWhereExpression())) {
            commandText += " WHERE " + transfer.getWhereExpression();
        }

        if (StringUtils.isNoneEmpty(transfer.getOrderExpression())) {
            commandText += " ORDER BY " + transfer.getOrderExpression();
        }

        if (StringUtils.isNoneEmpty(transfer.getLimitExpression())) {
            commandText += " LIMIT " + transfer.getLimitExpression();
        }

        this.parameterHandlers = transfer.getJdbcHandlers();
        this.parameterSequences = transfer.getParameters();
        this.parameterLength = this.parameterHandlers.size();
        return this;
    }

    private String selectBody(TingerMetadata<?> metadata) {
        List<String> names = new LinkedList<>();
        metadata.getProperties().forEach(x -> names.add("`" + x.getColumn() + "`"));
        String columnNames = StringUtils.join(names, ", ");
        return "SELECT " + columnNames + " FROM `[]`.`[]`";
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

    public static SelectStatementCreatorTemplate build(Queryable queryable) {
        String named = queryable.metadata().getType().getSimpleName() + "_" + queryable.name();
        return BUFFER.get(named, () -> new SelectStatementCreatorTemplate().generate(queryable));
    }
}
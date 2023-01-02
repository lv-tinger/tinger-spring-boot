package org.tinger.jdbc.mysql;

import org.tinger.common.utils.StringUtils;
import org.tinger.jdbc.dialect.JdbcDialect;
import org.tinger.jdbc.dialect.JdbcExecuteContext;
import org.tinger.jdbc.handler.JdbcHandler;
import org.tinger.jdbc.metadata.JdbcMetadata;
import org.tinger.jdbc.metadata.JdbcProperty;
import org.tinger.jdbc.queryable.Criteria;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class MysqlJdbcDialect implements JdbcDialect {

    private static final String INSERT_SQL_TEMPLATE = "INSERT INTO `[]`.`[]` ([]) VALUES ([])";
    private static final String UPDATE_SQL_TEMPLATE = "UPDATE `[]`.`[]` SET []";
    private static final String DELETE_SQL_TEMPLATE = "DELETE FROM `[]`.`[]`";
    private static final String SELECT_SQL_TEMPLATE = "SELECT * FROM `[]`.`[]`";
    private static final String WHERE_SQL_TEMPLATE = "[] WHERE []";
    private static final String ORDER_SQL_TEMPLATE = "[] ORDER BY []";
    private static final String LIMIT_SQL_TEMPLATE = "[] LIMIT []";

    @Override
    public <T, K> void resolveToCreateCommand(JdbcExecuteContext<T, K> context) {
        JdbcMetadata<T, K> metadata = context.getMetadata();
        List<JdbcProperty> properties = metadata.getProperties();

        T document = context.getDocument();

        String db = context.calculateDatabase();
        String dt = context.calculateDatatable();

        List<Object> parameters = new LinkedList<>();
        List<JdbcHandler<?>> jdbcHandlers = new LinkedList<>();


        List<String> propertyNames = new LinkedList<>();
        for (JdbcProperty property : properties) {
            Object value = property.getValue(document);
            if (value == null) {
                continue;
            }

            parameters.add(value);
            jdbcHandlers.add(property.getHandler());
            propertyNames.add("`" + property.getColumn() + "`");
        }

        String cns = StringUtils.join(propertyNames, ", ");
        String pms = StringUtils.repeat("?", ", ", propertyNames.size());

        String command = StringUtils.format(INSERT_SQL_TEMPLATE, db, dt, cns, pms);

        context.setCommand(command);
        context.setParameters(parameters);
        context.setHandlers(jdbcHandlers);
    }

    @Override
    public <T, K> void resolveToSelectCommand(JdbcExecuteContext<T, K> context) {
        if (context.getPrimary() != null) {
            resolveToSelectCommandWithPrimaryKey(context);
        } else if (context.getCriteria() != null) {
            resolveToSelectCommandWithCriteria(context);
        } else {
            resolveToSelectCommandWithAll(context);
        }
    }

    private <T, K> void resolveToSelectCommandWithPrimaryKey(JdbcExecuteContext<T, K> context) {
        JdbcMetadata<T, K> metadata = context.getMetadata();

        String db = context.calculateDatabase();
        String dt = context.calculateDatatable();

        String command = StringUtils.format(SELECT_SQL_TEMPLATE, db, dt);

        K primaryValue = context.getPrimary();
        JdbcProperty primaryKey = metadata.getPrimaryKey();
        command = StringUtils.format(WHERE_SQL_TEMPLATE, command, "`" + primaryKey.getColumn() + "` = ?");
        List<Object> parameters = Collections.singletonList(primaryValue);
        List<JdbcHandler<?>> jdbcHandlers = Collections.singletonList(primaryKey.getHandler());

        context.setCommand(command);
        context.setParameters(parameters);
        context.setHandlers(jdbcHandlers);
    }

    private <T, K> void resolveToSelectCommandWithCriteria(JdbcExecuteContext<T, K> context) {
        JdbcMetadata<T, K> metadata = context.getMetadata();

        String db = context.calculateDatabase();
        String dt = context.calculateDatatable();

        String command = StringUtils.format(SELECT_SQL_TEMPLATE, db, dt);

        Criteria criteria = context.getCriteria();
        MysqlTransfer transfer = MysqlTransfer.builder().metadata(metadata).criteria(criteria).build().resolve();
        String whereExpression = transfer.getWhereExpression();
        command = StringUtils.format(WHERE_SQL_TEMPLATE, command, whereExpression);
        List<Object> parameters = transfer.getParameters();
        List<JdbcHandler<?>> jdbcHandlers = transfer.getJdbcHandlers();

        context.setCommand(command);
        context.setParameters(parameters);
        context.setHandlers(jdbcHandlers);
    }

    private <T, K> void resolveToSelectCommandWithAll(JdbcExecuteContext<T, K> context) {
        String db = context.calculateDatabase();
        String dt = context.calculateDatatable();

        String command = StringUtils.format(SELECT_SQL_TEMPLATE, db, dt);

        context.setCommand(command);
        context.setParameters(Collections.emptyList());
        context.setHandlers(Collections.emptyList());
    }

    @Override
    public <T, K> void resolveToUpdateCommand(JdbcExecuteContext<T, K> context) {
        if (context.getDocument() != null) {
            resolveToUpdateCommandWithDocumentId(context);
        } else if (context.getCriteria() != null) {
            resolveToUpdateCommandWithUpdate(context);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private <T, K> void resolveToUpdateCommandWithDocumentId(JdbcExecuteContext<T, K> context) {
        JdbcMetadata<T, K> metadata = context.getMetadata();
        List<JdbcProperty> properties = metadata.getProperties();
        JdbcProperty primaryKey = metadata.getPrimaryKey();

        T document = context.getDocument();

        String db = context.calculateDatabase();
        String dt = context.calculateDatatable();

        List<Object> parameters = new LinkedList<>();
        List<JdbcHandler<?>> jdbcHandlers = new LinkedList<>();

        List<String> propertyNames = new LinkedList<>();
        for (JdbcProperty property : properties) {
            Object value = property.getValue(document);
            if (value == null || Objects.equals(property, primaryKey)) {
                continue;
            }

            parameters.add(value);
            jdbcHandlers.add(property.getHandler());
            propertyNames.add("`" + property.getColumn() + "` = ?");
        }
        String cns = StringUtils.join(propertyNames, ", ");

        String pms = "`" + primaryKey.getColumn() + "` = ?";
        Object primaryValue = primaryKey.getValue(document);
        parameters.add(primaryValue);
        jdbcHandlers.add(primaryKey.getHandler());

        String command = StringUtils.format(UPDATE_SQL_TEMPLATE, db, dt, cns);
        command = StringUtils.format(WHERE_SQL_TEMPLATE, command, pms);

        context.setCommand(command);
        context.setParameters(parameters);
        context.setHandlers(jdbcHandlers);
    }

    private <T, K> void resolveToUpdateCommandWithUpdate(JdbcExecuteContext<T, K> context) {
        JdbcMetadata<T, K> metadata = context.getMetadata();

        String db = context.calculateDatabase();
        String dt = context.calculateDatatable();

        MysqlTransfer transfer = MysqlTransfer.builder().update(context.getUpdate()).metadata(metadata).criteria(context.getCriteria()).build();
        transfer.resolve();

        String command = StringUtils.format(UPDATE_SQL_TEMPLATE, db, dt, transfer.getUpdateExpression());
        command = StringUtils.format(WHERE_SQL_TEMPLATE, command, transfer.getWhereExpression());

        context.setCommand(command);
        context.setParameters(transfer.getParameters());
        context.setHandlers(transfer.getJdbcHandlers());
    }

    @Override
    public <T, K> void resolveToDeleteCommand(JdbcExecuteContext<T, K> context) {
        if (context.getDocument() != null) {
            resolveToDeleteCommandWithDocument(context);
        } else if (context.getCriteria() != null) {
            resolveToDeleteCommandWithCriteria(context);
        }
    }

    private <T, K> void resolveToDeleteCommandWithDocument(JdbcExecuteContext<T, K> context) {
        JdbcMetadata<T, K> metadata = context.getMetadata();

        String db = context.calculateDatabase();
        String dt = context.calculateDatatable();

        String command = StringUtils.format(DELETE_SQL_TEMPLATE, db, dt);

        K primaryValue = context.getPrimary();
        JdbcProperty primaryKey = metadata.getPrimaryKey();
        command = StringUtils.format(WHERE_SQL_TEMPLATE, command, "`" + primaryKey.getColumn() + "` = ?");
        List<Object> parameters = Collections.singletonList(primaryValue);
        List<JdbcHandler<?>> jdbcHandlers = Collections.singletonList(primaryKey.getHandler());

        context.setCommand(command);
        context.setParameters(parameters);
        context.setHandlers(jdbcHandlers);
    }

    private <T, K> void resolveToDeleteCommandWithCriteria(JdbcExecuteContext<T, K> context) {
        JdbcMetadata<T, K> metadata = context.getMetadata();

        String db = context.calculateDatabase();
        String dt = context.calculateDatatable();

        String command = StringUtils.format(DELETE_SQL_TEMPLATE, db, dt);

        Criteria criteria = context.getCriteria();
        MysqlTransfer transfer = MysqlTransfer.builder().metadata(metadata).criteria(criteria).build().resolve();
        String whereExpression = transfer.getWhereExpression();
        command = StringUtils.format(WHERE_SQL_TEMPLATE, command, whereExpression);
        List<Object> parameters = transfer.getParameters();
        List<JdbcHandler<?>> jdbcHandlers = transfer.getJdbcHandlers();

        context.setCommand(command);
        context.setParameters(parameters);
        context.setHandlers(jdbcHandlers);
    }
}

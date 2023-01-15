package org.tinger.data.jdbc.context;

import org.tinger.common.utils.StringUtils;
import org.tinger.data.core.meta.TingerMetadata;
import org.tinger.data.core.meta.TingerProperty;
import org.tinger.data.jdbc.handler.JdbcHandler;

import java.util.LinkedList;
import java.util.List;

public class JdbcCreateExecuteContext extends AbstractJdbcExecuteContext {

    private static final String CREATE_TEMPLATE = "INSERT INTO [] ([]) VALUES ([])";

    public JdbcCreateExecuteContext(Object document, TingerMetadata<?> metadata, String database, String datatable) {
        super(document, metadata, database, datatable);
    }

    @Override
    public JdbcExecuteContext resolve() throws Exception {
        List<Object> parameterValues = this.getParameterValues();
        List<JdbcHandler<?>> jdbcHandlers = this.getParameterHandlers();
        List<String> propertyNames = new LinkedList<>();

        TingerProperty primaryKey = metadata.getPrimaryKey();
        parameterValues.add(primaryKey.getValue(this.document));
        jdbcHandlers.add(primaryKey.getHandler());
        propertyNames.add("`" + primaryKey.getColumn() + "`");

        List<TingerProperty> properties = metadata.getProperties();
        for (TingerProperty property : properties) {
            Object value = property.getValue(document);

            parameterValues.add(value);
            jdbcHandlers.add(property.getHandler());
            propertyNames.add("`" + property.getColumn() + "`");
        }

        String cns = StringUtils.join(propertyNames, ", ");
        String pms = StringUtils.repeat("?", ", ", propertyNames.size());

        String command = StringUtils.format(CREATE_TEMPLATE, database, datatable, cns, pms);
        this.setCommandText(command);

        return this;
    }
}
package org.tinger.data.jdbc.context;

import org.tinger.common.utils.StringUtils;
import org.tinger.data.core.meta.TingerMetadata;
import org.tinger.data.core.meta.TingerProperty;
import org.tinger.data.jdbc.handler.JdbcHandler;

import java.util.LinkedList;
import java.util.List;

public class JdbcCreateExecuteBatchContext extends AbstractJdbcExecuteBatchContext {
    private static final String CREATE_TEMPLATE = "INSERT INTO [] ([]) VALUES ([])";

    public JdbcCreateExecuteBatchContext(List<Object> documents, TingerMetadata<?> metadata, String database, String datatable) {
        super(documents, metadata, database, datatable);
    }

    @Override
    public JdbcExecuteBatchContext resolve() throws Exception {

        List<List<Object>> parameterValues = this.getParameterValues();
        List<JdbcHandler<?>> jdbcHandlers = this.getParameterHandlers();
        List<String> propertyNames = new LinkedList<>();

        TingerProperty primaryKey = metadata.getPrimaryKey();
        jdbcHandlers.add(primaryKey.getHandler());
        propertyNames.add("`" + primaryKey.getColumn() + "`");

        List<TingerProperty> properties = metadata.getProperties();
        for (TingerProperty property : properties) {
            jdbcHandlers.add(property.getHandler());
            propertyNames.add("`" + property.getColumn() + "`");
        }
        String cns = StringUtils.join(propertyNames, ", ");
        String pms = StringUtils.repeat("?", ", ", propertyNames.size());
        String command = StringUtils.format(CREATE_TEMPLATE, database, datatable, cns, pms);
        this.setCommandText(command);

        for (Object document : this.documents) {
            List<Object> parameterValue = new LinkedList<>();
            parameterValue.add(primaryKey.getValue(document));
            for (TingerProperty property : properties) {
                parameterValue.add(property.getValue(document));
            }
            parameterValues.add(parameterValue);
        }

        return this;
    }
}

package org.tinger.data.jdbc.context;

import org.tinger.common.utils.StringUtils;
import org.tinger.data.core.meta.TingerMetadata;
import org.tinger.data.core.meta.TingerProperty;
import org.tinger.data.jdbc.handler.JdbcHandler;

import java.util.LinkedList;
import java.util.List;

public class JdbcUpdateExecuteBatchContext extends AbstractJdbcExecuteBatchContext {
    private static final String UPDATE_TEMPLATE = "UPDATE `[]`.`[]` SET [] WHERE `[]` = ?";

    public JdbcUpdateExecuteBatchContext(List<Object> documents, TingerMetadata<?> metadata, String database, String datatable) {
        super(documents, metadata, database, datatable);
    }

    @Override
    public JdbcExecuteBatchContext resolve() throws Exception {
        TingerProperty primaryKey = this.metadata.getPrimaryKey();
        List<TingerProperty> properties = this.metadata.getProperties();
        List<JdbcHandler<?>> parameterHandlers = this.getParameterHandlers();

        List<String> propertyNames = new LinkedList<>();
        for (TingerProperty property : properties) {
            parameterHandlers.add(property.getHandler());
            propertyNames.add("`" + property.getColumn() + "` = ?");
        }
        parameterHandlers.add(primaryKey.getHandler());

        List<List<Object>> parameterValues = this.getParameterValues();
        for (Object document : documents) {
            List<Object> parameterValue = new LinkedList<>();
            for (TingerProperty property : properties) {
                parameterValue.add(property.getValue(document));
            }
            parameterValue.add(primaryKey.getValue(document));
            parameterValues.add(parameterValue);
        }

        String cns = StringUtils.join(propertyNames, ", ");
        String commandText = StringUtils.format(UPDATE_TEMPLATE, this.database, this.datatable, cns, primaryKey.getColumn());
        this.setCommandText(commandText);

        return this;
    }
}
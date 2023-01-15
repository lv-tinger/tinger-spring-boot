package org.tinger.data.jdbc.context;

import org.tinger.common.utils.StringUtils;
import org.tinger.data.core.meta.TingerMetadata;
import org.tinger.data.core.meta.TingerProperty;
import org.tinger.data.jdbc.handler.JdbcHandler;

import java.util.LinkedList;
import java.util.List;

public class JdbcUpdateExecuteContext extends AbstractJdbcExecuteContext {

    private static final String UPDATE_TEMPLATE = "UPDATE `[]`.`[]` SET [] WHERE `[]` = ?";

    public JdbcUpdateExecuteContext(Object document, TingerMetadata<?> metadata, String database, String datatable) {
        super(document, metadata, database, datatable);
    }

    @Override
    public JdbcExecuteContext resolve() throws Exception {
        TingerProperty primaryKey = this.metadata.getPrimaryKey();
        List<TingerProperty> properties = this.metadata.getProperties();

        List<JdbcHandler<?>> parameterHandlers = this.getParameterHandlers();
        List<Object> parameterValues = this.getParameterValues();

        List<String> propertyNames = new LinkedList<>();
        for (TingerProperty property : properties) {
            Object value = property.getValue(this.document);
            parameterValues.add(value);
            parameterHandlers.add(property.getHandler());
            propertyNames.add("`" + property.getColumn() + "` = ?");
        }
        parameterValues.add(primaryKey.getValue(this.document));
        parameterHandlers.add(primaryKey.getHandler());

        String cns = StringUtils.join(propertyNames, ", ");
        String commandText = StringUtils.format(UPDATE_TEMPLATE, this.database, this.datatable, cns, primaryKey.getColumn());
        this.setCommandText(commandText);
        return this;
    }
}

package org.tinger.data.jdbc.statement.template;

public abstract class CustomerStatementCreatorTemplate implements StatementCreatorTemplate {
    public static final String UPDATE = "UPDATE";
    public static final String CREATE = "CREATE";

    public abstract String getOperation();

    public abstract Class<?> getType();
}
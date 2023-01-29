package org.tinger.data.jdbc.statement;

public abstract class CustomerStatementCreator implements StatementCreator {
    public static final String UPDATE = "UPDATE";
    public static final String CREATE = "CREATE";

    public abstract String getOperation();

    public abstract Class<?> getType();
}
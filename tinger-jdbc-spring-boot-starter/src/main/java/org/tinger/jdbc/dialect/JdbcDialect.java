package org.tinger.jdbc.dialect;

public interface JdbcDialect {
    <T, K> void resolveToCreateCommand(JdbcExecuteContext<T, K> context);

    <T, K> void resolveToSelectCommand(JdbcExecuteContext<T, K> context);

    <T, K> void resolveToUpdateCommand(JdbcExecuteContext<T, K> context);

    <T, K> void resolveToDeleteCommand(JdbcExecuteContext<T, K> context);
}
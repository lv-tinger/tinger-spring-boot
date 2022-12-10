package org.tinger.jdbc.exception;

public class JdbcHandlerException extends RuntimeException {
    public JdbcHandlerException(int columnIndex, Throwable throwable) {
        super(throwable);
    }
}

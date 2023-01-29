package org.tinger.data.jdbc.statement;

import java.sql.Connection;
import java.sql.PreparedStatement;

public interface StatementCreator {
    PreparedStatement statement(Connection connection, String database, String datatable, Object parameter) throws Exception;
}
package org.tinger.data.jdbc.statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public interface StatementBatchCreator {
    List<PreparedStatement> statement(Connection connection) throws Exception;
}
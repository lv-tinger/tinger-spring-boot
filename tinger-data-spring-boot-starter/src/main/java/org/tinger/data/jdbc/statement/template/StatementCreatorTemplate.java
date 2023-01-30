package org.tinger.data.jdbc.statement.template;

import java.sql.Connection;
import java.sql.PreparedStatement;

public interface StatementCreatorTemplate {
    PreparedStatement statement(Connection connection, String database, String datatable, Object parameter) throws Exception;
}
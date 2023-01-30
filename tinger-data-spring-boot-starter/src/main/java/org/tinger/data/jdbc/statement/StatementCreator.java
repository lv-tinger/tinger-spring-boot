package org.tinger.data.jdbc.statement;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Created by tinger on 2023-01-30
 */
public interface StatementCreator {
    PreparedStatement statement(Connection connection) throws Exception;
}

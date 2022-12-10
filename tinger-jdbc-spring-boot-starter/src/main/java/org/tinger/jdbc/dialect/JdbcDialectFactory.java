package org.tinger.jdbc.dialect;

import org.tinger.jdbc.core.JdbcDriver;
import org.tinger.jdbc.mysql.MysqlJdbcDialect;

import java.util.HashMap;
import java.util.Map;

public class JdbcDialectFactory {
    private final Map<JdbcDriver, JdbcDialect> dialectMapper = new HashMap<>();

    private JdbcDialectFactory() {
        dialectMapper.put(JdbcDriver.MYSQL, new MysqlJdbcDialect());
    }

    public JdbcDialect getDialect(JdbcDriver driver) {
        return this.dialectMapper.get(driver);
    }

    private static final JdbcDialectFactory factory = new JdbcDialectFactory();

    public static JdbcDialectFactory getInstance() {
        return factory;
    }

}

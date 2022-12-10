package org.tinger.jdbc.queryable;

/**
 * Created by tinger on 2022-10-16
 */
public enum Operation {
    GT(">"), GE(">="),
    LT("<"), LE("<="),
    EQ("="), NEQ("!="),
    NULL(""), NON(""),
    IN(""), NIN(""),
    AND("AND"), OR("OR");
    public final String code;

    Operation(String code) {
        this.code = code;
    }
}

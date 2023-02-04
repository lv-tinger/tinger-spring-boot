package org.tinger.data.core.tsql;

/**
 * Created by tinger on 2022-10-16
 */
public enum Operation {
    GT(">"), GE(">="),
    LT("<"), LE("<="),
    EQ("="), NEQ("!="),
    NULL(""), NON(""),
    IN(""), NIN(""),
    AND("AND"), OR("OR"),
    ASC("ASC"), DESC("DESC");
    public final String code;

    Operation(String code) {
        this.code = code;
    }
}

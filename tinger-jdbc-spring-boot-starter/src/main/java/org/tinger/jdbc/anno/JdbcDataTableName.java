package org.tinger.jdbc.anno;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
public @interface JdbcDataTableName {
    String value();
}

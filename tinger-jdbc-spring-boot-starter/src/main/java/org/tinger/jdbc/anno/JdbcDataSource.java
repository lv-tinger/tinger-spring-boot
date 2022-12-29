package org.tinger.jdbc.anno;

import org.tinger.jdbc.core.JdbcDriver;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
public @interface JdbcDataSource {
    String value();
}

package org.tinger.jdbc.anno;

import java.lang.annotation.*;

/**
 * Created by tinger on 2022-10-20
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface JdbcDataColumn {
    String name();
}

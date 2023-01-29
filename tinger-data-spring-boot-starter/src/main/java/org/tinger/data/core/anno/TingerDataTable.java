package org.tinger.data.core.anno;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
public @interface TingerDataTable {
    String value();

    boolean shard() default false;
}

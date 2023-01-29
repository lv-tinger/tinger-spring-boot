package org.tinger.data.core.anno;

import java.lang.annotation.*;

/**
 * Created by tinger on 2022-10-20
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface TingerDataColumn {
    String value();
}

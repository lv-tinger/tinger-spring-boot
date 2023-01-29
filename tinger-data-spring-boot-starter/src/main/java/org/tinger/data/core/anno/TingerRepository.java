package org.tinger.data.core.anno;

import org.tinger.data.core.value.TingerDriver;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
public @interface TingerRepository {
    TingerDriver driver();
}
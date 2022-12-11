package org.tinger.jdbc.core;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.tinger.jdbc.anno.JdbcPrimaryKey;

import java.io.Serializable;

/**
 * Created by tinger on 2022-10-17
 */
@Getter
@Setter
@SuperBuilder
public abstract class JdbcEntity<K>  implements Serializable {
    @JdbcPrimaryKey
    private K id;
}

package org.tinger.jdbc.core;


import org.tinger.jdbc.anno.JdbcPrimaryKey;

/**
 * Created by tinger on 2022-10-17
 */
public class JdbcEntity<K> {
    @JdbcPrimaryKey
    private K id;

    public K getId() {
        return id;
    }

    public void setId(K id) {
        this.id = id;
    }
}

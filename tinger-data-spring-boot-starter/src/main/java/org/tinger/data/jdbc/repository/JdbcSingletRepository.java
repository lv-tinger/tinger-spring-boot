package org.tinger.data.jdbc.repository;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JdbcSingletRepository<T, K> extends TingerJdbcRepository<T, K> {
    private JdbcNamespace namespace;
}
package org.tinger.data.jdbc.repository;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JdbcClusterRepository<T, K> extends TingerJdbcRepository<T, K> {
    private ShardAlgorithm algorithm;
}

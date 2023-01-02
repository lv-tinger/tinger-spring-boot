package org.tinger.services.warrant.dao;

import org.tinger.jdbc.anno.JdbcDataSource;
import org.tinger.jdbc.anno.JdbcDataTableName;
import org.tinger.jdbc.anno.JdbcDatabaseName;
import org.tinger.jdbc.anno.JdbcRepository;
import org.tinger.jdbc.repository.AbstractJdbcDynamicRepository;
import org.tinger.services.warrant.po.Identity;

@JdbcRepository
@JdbcDataSource(value = "tinger")
@JdbcDatabaseName(value = "tinger")
@JdbcDataTableName(value = "identity")
public class IdentityRepository extends AbstractJdbcDynamicRepository<Identity, Long> {
}

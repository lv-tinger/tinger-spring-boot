package org.tinger.services.common.dao;

import org.springframework.stereotype.Component;
import org.tinger.jdbc.anno.JdbcDataSource;
import org.tinger.jdbc.anno.JdbcDataTableName;
import org.tinger.jdbc.anno.JdbcDatabaseName;
import org.tinger.jdbc.repository.AbstractJdbcStaticsRepository;
import org.tinger.services.common.po.Version;


@Component
@JdbcDataSource("tinger")
@JdbcDatabaseName("app_config")
@JdbcDataTableName("version")
public class VersionDao extends AbstractJdbcStaticsRepository<Version, String> {
}

package org.tinger.services.apps.dao;

import org.springframework.stereotype.Component;
import org.tinger.jdbc.anno.JdbcDataSource;
import org.tinger.jdbc.anno.JdbcDataTableName;
import org.tinger.jdbc.anno.JdbcDatabaseName;
import org.tinger.jdbc.repository.AbstractJdbcStaticsRepository;
import org.tinger.services.apps.po.AppSetting;

@Component
@JdbcDataSource("tinger")
@JdbcDatabaseName("app_config")
@JdbcDataTableName("setting")
public class AppSettingDao extends AbstractJdbcStaticsRepository<AppSetting, String> {

}

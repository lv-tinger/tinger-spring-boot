package org.tinger.services.apps.dao;

import org.springframework.stereotype.Component;
import org.tinger.jdbc.anno.JdbcDataSource;
import org.tinger.jdbc.anno.JdbcDataTableName;
import org.tinger.jdbc.anno.JdbcDatabaseName;
import org.tinger.jdbc.queryable.Criteria;
import org.tinger.jdbc.queryable.Operation;
import org.tinger.jdbc.repository.AbstractJdbcStaticsRepository;
import org.tinger.services.apps.po.AppViewPart;

import java.util.List;

@Component
@JdbcDataSource("tinger")
@JdbcDatabaseName("app_config")
@JdbcDataTableName("view_part")
public class AppViewPartDao extends AbstractJdbcStaticsRepository<AppViewPart, String> {
    public List<AppViewPart> selectByAppId(String appId) {
        Criteria criteria = Criteria.where("appId", Operation.EQ, appId);
        return this.select(criteria);
    }
}

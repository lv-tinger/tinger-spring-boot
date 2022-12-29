package org.tinger.serv.apps.dao;

import org.springframework.stereotype.Component;
import org.tinger.jdbc.anno.JdbcDataSource;
import org.tinger.jdbc.anno.JdbcDataTableName;
import org.tinger.jdbc.anno.JdbcDatabaseName;
import org.tinger.jdbc.queryable.Criteria;
import org.tinger.jdbc.queryable.Operation;
import org.tinger.jdbc.repository.AbstractJdbcStaticsRepository;
import org.tinger.serv.apps.po.AppAgreement;

import java.util.List;

@Component
@JdbcDataSource("tinger")
@JdbcDatabaseName("app_config")
@JdbcDataTableName("agreement")
public class AppAgreementDao extends AbstractJdbcStaticsRepository<AppAgreement, String> {
    public List<AppAgreement> loadByAppId(String appId){
        Criteria criteria = Criteria.where("appId", Operation.EQ, appId);
        return this.select(criteria);
    }
}
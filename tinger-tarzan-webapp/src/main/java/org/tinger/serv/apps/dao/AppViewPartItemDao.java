package org.tinger.serv.apps.dao;

import org.springframework.stereotype.Component;
import org.tinger.jdbc.anno.JdbcDataSource;
import org.tinger.jdbc.anno.JdbcDataTableName;
import org.tinger.jdbc.anno.JdbcDatabaseName;
import org.tinger.jdbc.queryable.Criteria;
import org.tinger.jdbc.queryable.Operation;
import org.tinger.jdbc.repository.AbstractJdbcStaticsRepository;
import org.tinger.serv.apps.po.AppViewPartItem;

import java.util.List;

@Component
@JdbcDataSource("tinger")
@JdbcDatabaseName("app_config")
@JdbcDataTableName("view_part_item")
public class AppViewPartItemDao extends AbstractJdbcStaticsRepository<AppViewPartItem, String> {
    public List<AppViewPartItem> loadByPartId(String partId){
        Criteria criteria = Criteria.where("partId", Operation.EQ, partId);
        return this.select(criteria);
    }
}
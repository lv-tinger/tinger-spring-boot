package org.tinger.data.jdbc.namespace;

import org.tinger.common.algorithm.round.RoundRobin;
import org.tinger.common.algorithm.round.RoundRobinFactory;
import org.tinger.data.jdbc.source.wrapper.TingerJdbcDataSourceWrapper;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

public class JdbcNamespaceBuilder {

    public static JdbcNamespace build(List<TingerJdbcDataSourceWrapper> sourceWrappers){
        List<TingerJdbcDataSourceWrapper> masters = sourceWrappers.stream().filter(TingerJdbcDataSourceWrapper::isMaster).toList();
        if (masters.size() != 1) {
            throw new RuntimeException();
        }
        TingerJdbcDataSourceWrapper master = masters.get(0);
        List<TingerJdbcDataSourceWrapper> slavers = sourceWrappers.stream().filter(TingerJdbcDataSourceWrapper::isSlaver).toList();
        if (slavers.size() == 0) {
            return new SimpleJdbcNamespace(master.getSource());
        } else if (slavers.size() == 1) {
            return new ReadWriteJdbcNamespace(master.getSource(), slavers.get(0).getSource());
        } else {
            List<Integer> slaverWeights = slavers.stream().map(TingerJdbcDataSourceWrapper::getWeight).toList();
            String impl = slaverWeights.stream().anyMatch(x -> x == null || x == 0) ? RoundRobinFactory.COMMON : RoundRobinFactory.STATIC_WEIGHT;
            List<DataSource> slaverSources = slavers.stream().map(TingerJdbcDataSourceWrapper::getSource).toList();
            RoundRobin<DataSource> slaver = RoundRobinFactory.build(impl, slaverSources, slaverWeights);
            return new ReadWriteRoundRobinJdbcNamespace(master.getSource(), slaver);
        }
    }
    public static JdbcNamespace build(List<TingerJdbcDataSourceWrapper> sourceWrappers, String database, String datatable) {
        List<TingerJdbcDataSourceWrapper> masters = sourceWrappers.stream().filter(TingerJdbcDataSourceWrapper::isMaster).toList();
        if (masters.size() != 1) {
            throw new RuntimeException();
        }
        TingerJdbcDataSourceWrapper master = masters.get(0);
        List<TingerJdbcDataSourceWrapper> slavers = sourceWrappers.stream().filter(TingerJdbcDataSourceWrapper::isSlaver).toList();
        if (slavers.size() == 0) {
            return new SimpleJdbcNamespace(master.getSource(), database, datatable);
        } else if (slavers.size() == 1) {
            return new ReadWriteJdbcNamespace(master.getSource(), slavers.get(0).getSource(), database, datatable);
        } else  {
            List<Integer> slaverWeights = slavers.stream().map(TingerJdbcDataSourceWrapper::getWeight).toList();
            String impl = slavers.stream().map(TingerJdbcDataSourceWrapper::getWeight).distinct().count() == 1
                    ? RoundRobinFactory.COMMON
                    : RoundRobinFactory.STATIC_WEIGHT;
            List<DataSource> slaverSources = slavers.stream().map(TingerJdbcDataSourceWrapper::getSource).toList();
            RoundRobin<DataSource> slaver = RoundRobinFactory.build(impl, slaverSources, slaverWeights);
            return new ReadWriteRoundRobinJdbcNamespace(master.getSource(), slaver, database, datatable);
        }
    }
}

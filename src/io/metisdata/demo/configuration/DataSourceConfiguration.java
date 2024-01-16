package io.metisdata.demo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;

import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.listener.QueryExecutionListener;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import net.ttddyy.dsproxy.proxy.ParameterSetOperation;

@Configuration
public class DataSourceConfiguration {
    
    @Bean
    public DataSource getDataSource() {
        DataSource actual = DataSourceBuilder.create()
            .url("jdbc:postgresql://127.0.0.1:5432/demo")
            .driverClassName("org.postgresql.Driver")
            .username("postgres")
            .password("postgres")
            .build();
        return ProxyDataSourceBuilder.create(actual)
            .logQueryToSysOut()
            .listener(new QueryExecutionListener() {
                @Override
                public void beforeQuery(ExecutionInfo execInfo, List<QueryInfo> queryInfoList) {
                }

                @Override
                public void afterQuery(ExecutionInfo execInfo, List<QueryInfo> queryInfoList) {
                    System.out.println("Now query");
                    for(QueryInfo info : queryInfoList){
                        System.out.println(info.getQuery());
                        for(List<ParameterSetOperation> list : info.getParametersList()){
                            for(ParameterSetOperation parameterSet : list){
                                for(Object o : parameterSet.getArgs()){
                                    System.out.println(o);
                                }
                            }
                        }
                    }
                }
            })
            .build();  // returns ProxyDataSource instance
    }
}
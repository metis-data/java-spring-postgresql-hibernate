package io.metisdata.demo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.Arrays;

import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;

import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.listener.QueryExecutionListener;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import net.ttddyy.dsproxy.proxy.ParameterSetOperation;

import io.opentelemetry.sdk.autoconfigure.AutoConfiguredOpenTelemetrySdk;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.context.Scope;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.api.trace.TracerProvider;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.api.GlobalOpenTelemetry;

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
                    OpenTelemetry openTelemetry = GlobalOpenTelemetry.get();

                    Tracer tracer = openTelemetry.getTracer("io.metisdata.demo.configuration.DataSourceConfiguration");

                    for(QueryInfo info : queryInfoList){
                        String queryWithPlaceholders = info.getQuery();
                        System.out.println(queryWithPlaceholders);
                        List<Object> parameters = info
                            .getParametersList().stream()
                            .flatMap(l -> 
                                l.stream().map(o -> o.getArgs()[1])
                            ).collect(Collectors.toList());

                        String[] queryParts = queryWithPlaceholders.split("[?]");
                        String finalQuery = String.join("", IntStream.range(0, queryParts.length)
                            .mapToObj(i -> queryParts[i].toString() + 
                                (i < parameters.size() 
                                    ? parameters.get(i) instanceof String
                                        ? "'" + parameters.get(i).toString() + "'"
                                        : parameters.get(i).toString() 
                                    : "")
                            )
                            .collect(Collectors.toList())
                        );
                        
                        System.out.println(finalQuery);

                        Span span = tracer.spanBuilder("/java-trace").setSpanKind(SpanKind.CLIENT).startSpan();
                        Scope scope = span.makeCurrent();
                        span.setAttribute("db.statement", finalQuery);
                        span.end();
                    }
                }
            })
            .build();
    }
}
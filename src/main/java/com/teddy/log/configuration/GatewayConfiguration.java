package com.teddy.log.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import com.teddy.log.service.EsLogProcessService;

import reactor.core.publisher.Mono;

/**
 * package com.teddy.log.configuration <br/>
 * description: 网关配置 <br/>
 *
 * @author fanzhongwei
 * @date 20-3-31
 */
@Configuration
public class GatewayConfiguration {

    @Value("${elasticsearch}")
    private String esUrl;

    @Autowired
    private EsLogProcessService esLogProcessService;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("es_route", r -> r.path("/*")
                .and()
                .header("User-Agent", "Go-http-client/1.1")
                .filters(f -> f.modifyResponseBody(String.class, String.class, MediaType.APPLICATION_JSON_VALUE,
                    (exchange, s) -> Mono.just(esLogProcessService.processLog(s))))
                .uri(esUrl))
            .route("log_message_parse", r -> r.path("/*")
                .and()
                .header("User-Agent", "Go-http-client/1.1")
                .filters(f -> f.prefixPath("/_bulk")
                    .modifyRequestBody(String.class, String.class, MediaType.APPLICATION_JSON_VALUE,
                        (exchange, s) -> Mono.just(esLogProcessService.processLog(s)))
                    .modifyResponseBody(String.class, String.class, MediaType.APPLICATION_JSON_VALUE,
                        (exchange, s) -> Mono.just(esLogProcessService.processLog(s))))
                .uri(esUrl))
            .build();
    }
}

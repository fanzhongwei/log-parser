package com.teddy.log.configuration;

import org.apache.commons.lang3.StringUtils;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(GatewayConfiguration.class);

    @Value("${elasticsearch}")
    private String esUrl;

    @Autowired
    private EsLogProcessService esLogProcessService;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("log_message_parse", r -> r.path("/_bulk")
                        .and()
                        .header("User-Agent", "Go-http-client/1.1")
                        .filters(f -> f
                                .modifyRequestBody(String.class, String.class, MediaType.APPLICATION_JSON_VALUE,
                                        (exchange, s) -> Mono.justOrEmpty(esLogProcessService.processLog(s)))
                                .modifyResponseBody(String.class, String.class, MediaType.APPLICATION_JSON_VALUE,
                                        (exchange, s) -> printResponse(exchange.getRequest().getURI().toString(), s)))
                        .uri(esUrl))
                .route("es_route", r -> r.path("/**")
                        .and()
                        .header("User-Agent", "Go-http-client/1.1")
                        .filters(f -> f
                                .modifyRequestBody(String.class, String.class, MediaType.APPLICATION_JSON_VALUE,
                                        (exchange, s) -> printRequest(exchange.getRequest().getURI().toString(), s))
                                .modifyResponseBody(String.class, String.class, MediaType.APPLICATION_JSON_VALUE,
                                        (exchange, s) -> printResponse(exchange.getRequest().getURI().toString(), s)))
                        .uri(esUrl))
                .build();
    }

    private Publisher<String> printRequest(String url, String s) {
        logger.debug("send [{}] to [{}] request body is: {}", url, esUrl, s);
        return Mono.justOrEmpty(s);
    }

    private Publisher<String> printResponse(String url, String s) {
        logger.debug("url [{}] response body is: {}", esUrl, s);
        return Mono.justOrEmpty(s);
    }
}

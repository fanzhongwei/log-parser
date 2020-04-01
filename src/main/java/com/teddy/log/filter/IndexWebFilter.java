package com.teddy.log.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

/**
 * package com.teddy.log.filter <br/>
 * description: home page <br/>
 *
 * @author fanzhongwei
 * @date 20-4-1
 */
@Component
public class IndexWebFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        if (serverWebExchange.getRequest()
            .getURI()
            .getPath()
            .equals("/")
            && !serverWebExchange.getRequest()
                .getHeaders()
                .containsValue("Go-http-client/1.1")) {
            return webFilterChain.filter(serverWebExchange.mutate()
                .request(serverWebExchange.getRequest()
                    .mutate()
                    .path("/index.html")
                    .build())
                .build());
        }
        return webFilterChain.filter(serverWebExchange);
    }
}

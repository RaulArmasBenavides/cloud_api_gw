package api.gw.cloud_api_gw;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
class CookieToAuthHeaderFilter implements GlobalFilter, Ordered {
    private final String cookieName;

    public CookieToAuthHeaderFilter(
            @Value("${auth.cookie.name:ACCESS_TOKEN}") String cookieName) { // <-- aquí
        this.cookieName = cookieName;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
            org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        HttpCookie c = exchange.getRequest().getCookies().getFirst(cookieName);
        if (c != null && !c.getValue().isBlank()) {
            exchange = exchange.mutate()
                    .request(r -> r.headers(h -> h.setBearerAuth(c.getValue())))
                    .build();
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    } // antes de Security
}

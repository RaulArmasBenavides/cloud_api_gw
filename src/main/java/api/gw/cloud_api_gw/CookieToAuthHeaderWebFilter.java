package api.gw.cloud_api_gw;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CookieToAuthHeaderWebFilter implements WebFilter {

    private final String cookieName;

    public CookieToAuthHeaderWebFilter(@Value("${auth.cookie.name:ACCESS_TOKEN}") String cookieName) {
        this.cookieName = cookieName;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        // Si ya viene Authorization, no tocar
        String auth = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (auth != null && auth.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        HttpCookie c = exchange.getRequest().getCookies().getFirst(cookieName);
        if (c == null || c.getValue() == null || c.getValue().isBlank()) {
            return chain.filter(exchange);
        }

        String token = c.getValue();

        ServerWebExchange mutated = exchange.mutate()
            .request(r -> r.headers(h -> h.setBearerAuth(token)))
            .build();

        return chain.filter(mutated);
    }
}
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

    public CookieToAuthHeaderFilter(@Value("${auth.cookie.name:ACCESS_TOKEN}") String cookieName) {
        this.cookieName = cookieName;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();
        HttpCookie c = exchange.getRequest().getCookies().getFirst(cookieName);

        if (c == null || c.getValue().isBlank()) {
            System.out.println("[GW][Cookie->Auth] " + path + " cookie=" + cookieName + " ABSENT");
            return chain.filter(exchange);
        }

        // Log seguro: no imprimas el token completo
        String v = c.getValue();
        String preview = v.length() > 12 ? v.substring(0, 12) + "..." : v;
        System.out.println("[GW][Cookie->Auth] " + path + " cookie=" + cookieName + " PRESENT (len=" + v.length() + ", preview=" + preview + ")");

        ServerWebExchange mutated = exchange.mutate()
            .request(r -> r.headers(h -> h.setBearerAuth(v)))
            .build();

        System.out.println("[GW][Cookie->Auth] " + path + " -> Authorization: Bearer <set>");
        return chain.filter(mutated);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE; // antes de Security
    }
}
package api.gw.cloud_api_gw;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthRelayConfig {
    @Bean
    GlobalFilter authCookieToAuthHeader(
            @org.springframework.beans.factory.annotation.Value("${auth.cookie.name:ACCESS_TOKEN}") String cookieName) {
        return (exchange, chain) -> {
            var c = exchange.getRequest().getCookies().getFirst(cookieName);
            if (c != null && !c.getValue().isBlank()) {
                exchange = exchange.mutate()
                        .request(r -> r.headers(h -> h.setBearerAuth(c.getValue())))
                        .build();
            }
            return chain.filter(exchange);
        };
    }
}

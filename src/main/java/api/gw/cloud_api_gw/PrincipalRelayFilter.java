package api.gw.cloud_api_gw;

import java.util.List;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class PrincipalRelayFilter implements GlobalFilter, Ordered {
  private static final List<String> OPEN_PATHS = List.of(
      "/api/v2/login",
      "/api/v2/renewToken",
      "/api/v2/logout",
      "/api/v2/register");

  @Override
  public Mono<Void> filter(ServerWebExchange exchange,
      org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {

    String path = exchange.getRequest().getURI().getPath();

    // No tocar rutas abiertas (login, etc.)
    if (OPEN_PATHS.stream().anyMatch(path::startsWith)) {
      return chain.filter(exchange);
    }

    // Para el resto, si hay principal lo propagamos; si no, limpiamos credenciales
    // y seguimos
    return exchange.getPrincipal()
        .cast(Authentication.class)
        .flatMap(auth -> {
          ServerHttpRequest.Builder rb = exchange.getRequest().mutate();

          if (auth.getPrincipal() instanceof Jwt jwt) {
            String userId = jwt.getSubject();
            Object rolesObj = jwt.getClaims().get("roles");
            String roles = rolesObj == null ? ""
                : (rolesObj instanceof Iterable<?> it ? String.join(",", (Iterable<String>) it) : rolesObj.toString());

            rb.headers(h -> {
              h.add("X-User-Id", userId);
              h.add("X-User-Roles", roles);
              h.remove("Authorization");
              h.remove("Cookie");
            });
          } else {
            rb.headers(h -> {
              h.remove("Authorization");
              h.remove("Cookie");
            });
          }

          return chain.filter(exchange.mutate().request(rb.build()).build());
        })
        .switchIfEmpty(Mono.defer(() -> { // <-- reemplaza defaultIfEmpty(null)
          ServerHttpRequest mutated = exchange.getRequest().mutate()
              .headers(h -> {
                h.remove("Authorization");
                h.remove("Cookie");
              })
              .build();
          return chain.filter(exchange.mutate().request(mutated).build());
        }));
  }

  @Override
  public int getOrder() {
    return -10;
  }
}
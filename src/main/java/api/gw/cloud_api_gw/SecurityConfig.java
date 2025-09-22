package api.gw.cloud_api_gw;

// SecurityConfig.java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {
  @Bean
  SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    http.csrf(csrf -> csrf.disable());
    http.authorizeExchange(ex -> ex
        .pathMatchers("/api/v2/login", "/api/v2/renewToken", "/api/v2/logout", "/api/v2/register").permitAll()
        .anyExchange().authenticated()
    );
    http.oauth2ResourceServer(o -> o.jwt()); // valida Authorization: Bearer ...
    return http.build();
  }
}

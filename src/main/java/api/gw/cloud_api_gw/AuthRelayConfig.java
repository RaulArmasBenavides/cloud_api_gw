package api.gw.cloud_api_gw;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthRelayConfig {
    @Bean
    GlobalFilter authCookieToAuthHeader(
            @org.springframework.beans.factory.annotation.Value("${auth.cookie.name:ACCESS_TOKEN}") String cookieName) {
        return new CookieToAuthHeaderFilter(cookieName);
    }
}

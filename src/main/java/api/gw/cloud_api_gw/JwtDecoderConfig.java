package api.gw.cloud_api_gw;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;

@Configuration
public class JwtDecoderConfig {
  @Bean
  public org.springframework.security.oauth2.jwt.ReactiveJwtDecoder jwtDecoder(
      @Value("${security.jwt.secret}") String secret) {
    var key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
    return NimbusReactiveJwtDecoder.withSecretKey(key).build();
  }
}
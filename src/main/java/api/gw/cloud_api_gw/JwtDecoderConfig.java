package api.gw.cloud_api_gw;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

import io.jsonwebtoken.io.Decoders;

@Configuration
public class JwtDecoderConfig {

  @Bean
  public ReactiveJwtDecoder reactiveJwtDecoder(@Value("${security.jwt.secret}") String base64Secret) {
    byte[] keyBytes = io.jsonwebtoken.io.Decoders.BASE64.decode(base64Secret);
    var key = new SecretKeySpec(keyBytes, "HmacSHA256");
    return NimbusReactiveJwtDecoder.withSecretKey(key).build();
  }
}
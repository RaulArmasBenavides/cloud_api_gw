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
  // @Bean
  // public org.springframework.security.oauth2.jwt.ReactiveJwtDecoder jwtDecoder(
  // @Value("${security.jwt.secret}") String secret) {
  // var key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
  // return NimbusReactiveJwtDecoder.withSecretKey(key).build();
  // }

  // @Bean
  // ReactiveJwtDecoder jwtDecoder2(@Value("${security.jwt.secret}") String b64) {
  //   byte[] key = Decoders.BASE64.decode(b64);
  //   return NimbusReactiveJwtDecoder.withSecretKey(new javax.crypto.spec.SecretKeySpec(key, "HmacSHA256")).build();
  // }

  @Bean
  ReactiveJwtDecoder jwtDecoder(@Value("${security.jwt.secret}") String secretB64) {
    byte[] key = java.util.Base64.getDecoder().decode(secretB64.trim());
    return NimbusReactiveJwtDecoder
        .withSecretKey(new javax.crypto.spec.SecretKeySpec(key, "HmacSHA256"))
        .build();
  }
}
package engineering.everest.starterkit.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.security.SecureRandom;

@Configuration
public class SecurityConfig {

    private static final int DEFAULT_PASSWORD_ENCODER_STRENGTH = 10;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(DEFAULT_PASSWORD_ENCODER_STRENGTH, new SecureRandom());
    }

    @Bean
    JwtAccessTokenConverter jwtAccessTokenConverter(@Value("${application.jwt.signing-secret}") String jwtSigningSecret) {
        var jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(jwtSigningSecret);
        return jwtAccessTokenConverter;
    }
}

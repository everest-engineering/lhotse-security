package engineering.everest.starterkit.security.config;

import engineering.everest.starterkit.security.AuthenticationProvider;
import engineering.everest.starterkit.security.AuthenticationServerUserDetailsService;
import engineering.everest.starterkit.security.persistence.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableMongoRepositories("engineering.everest.starterkit.security.persistence")
public class Config {
    @Bean
    public AuthenticationKeyGenerator authenticationKeyGenerator() {
        return new DefaultAuthenticationKeyGenerator();
    }

    @Bean
    public TokenKeyGenerator tokenKeyGenerator() {
        return new TokenKeyGenerator();
    }

    @Bean
    public TokenStore mongoTokenStore(OAuthAccessTokenRepository oAuthAccessTokenRepository,
                                      OAuthRefreshTokenRepository oAuthRefreshTokenRepository,
                                      AuthenticationKeyGenerator authenticationKeyGenerator,
                                      TokenKeyGenerator tokenKeyGenerator,
                                      OAuth2Serializer oAuth2Serializer) {
        return new MongoTokenStore(oAuthAccessTokenRepository, oAuthRefreshTokenRepository,
                authenticationKeyGenerator, tokenKeyGenerator, oAuth2Serializer);
    }

    @Bean
    public OAuth2Serializer oAuth2Serializer() {
        return new OAuth2Serializer();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder,
                                                         AuthenticationServerUserDetailsService userDetailsService) {
        return new AuthenticationProvider(passwordEncoder, userDetailsService);
    }
}

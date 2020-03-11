package engineering.everest.starterkit.security.config;

import engineering.everest.starterkit.security.AuthenticationProvider;
import engineering.everest.starterkit.security.AuthenticationServerUserDetailsService;
import engineering.everest.starterkit.security.persistence.JpaAuthTokenStore;
import engineering.everest.starterkit.security.persistence.OAuth2Serializer;
import engineering.everest.starterkit.security.persistence.OAuthAccessTokenRepository;
import engineering.everest.starterkit.security.persistence.OAuthRefreshTokenRepository;
import engineering.everest.starterkit.security.persistence.TokenKeyGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
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
    @Qualifier("authTokenStore")
    public TokenStore authTokenStore(OAuthAccessTokenRepository oAuthAccessTokenRepository,
                                     OAuthRefreshTokenRepository oAuthRefreshTokenRepository,
                                     AuthenticationKeyGenerator authenticationKeyGenerator,
                                     TokenKeyGenerator tokenKeyGenerator,
                                     OAuth2Serializer oAuth2Serializer) {
        return new JpaAuthTokenStore(oAuthAccessTokenRepository, oAuthRefreshTokenRepository,
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

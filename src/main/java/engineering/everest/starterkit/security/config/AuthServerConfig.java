package engineering.everest.starterkit.security.config;

import engineering.everest.starterkit.security.AuthenticationServerUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final JwtAccessTokenConverter jwtAccessTokenConverter;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final TokenStore tokenStore;
    private final int accessTokenValiditySeconds;
    private final int refreshTokenValiditySeconds;

    @Autowired
    public AuthServerConfig(PasswordEncoder passwordEncoder,
                            JwtAccessTokenConverter jwtAccessTokenConverter,
                            AuthenticationServerUserDetailsService userDetailsService,
                            AuthenticationManager authenticationManager,
                            @Qualifier("authTokenStore") TokenStore tokenStore,
                            @Value("${application.jwt.access-token.validity-seconds:3600}") int accessTokenValiditySeconds,
                            @Value("${application.jwt.refresh-token.validity-seconds:7200}") int refreshTokenValiditySeconds) {
        super();
        this.passwordEncoder = passwordEncoder;
        this.jwtAccessTokenConverter = jwtAccessTokenConverter;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.tokenStore = tokenStore;
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer securityConfigurer) {
        securityConfigurer
                .passwordEncoder(passwordEncoder)
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .allowFormAuthenticationForClients();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clientsConfigurer) throws Exception {
        clientsConfigurer.inMemory()
                .withClient("web-app-ui")
                .secret(passwordEncoder.encode(""))
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("all")
                .accessTokenValiditySeconds(accessTokenValiditySeconds)
                .refreshTokenValiditySeconds(refreshTokenValiditySeconds)
                .authorities("ROLE_WEB_APP_UI")
                .autoApprove(true);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpointsConfigurer) {
        endpointsConfigurer
                .authenticationManager(authenticationManager)
                .accessTokenConverter(jwtAccessTokenConverter)
                .userDetailsService(userDetailsService)
                .tokenStore(tokenStore)
                .reuseRefreshTokens(false);
    }
}

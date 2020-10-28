package engineering.everest.starterkit.security.config;

import engineering.everest.starterkit.security.ApplicationUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


@Configuration
@Order(20)
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private final String[] anonymousUserAntPaths;
    private final String[] authenticatedUserAntPaths;
    private final String[] adminUserAntPaths;

    private final JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired
    public ResourceServerConfig(JwtAccessTokenConverter jwtAccessTokenConverter,
                                ApplicationUserDetailsService userDetailsService,
                                @Value("${application.security.endpoint.matchers.anonymous}") String[] anonymousUserAntPaths,
                                @Value("${application.security.endpoint.matchers.authenticated}") String[] authenticatedUserAntPaths,
                                @Value("${application.security.endpoint.matchers.admin}") String[] adminUserAntPaths) {
        super();
        var defaultUserAuthenticationConverter = new DefaultUserAuthenticationConverter();
        defaultUserAuthenticationConverter.setUserDetailsService(userDetailsService);
        var accessTokenConverter = (DefaultAccessTokenConverter) jwtAccessTokenConverter.getAccessTokenConverter();
        accessTokenConverter.setUserTokenConverter(defaultUserAuthenticationConverter);
        this.jwtAccessTokenConverter = jwtAccessTokenConverter;
        this.anonymousUserAntPaths = anonymousUserAntPaths;
        this.authenticatedUserAntPaths = authenticatedUserAntPaths;
        this.adminUserAntPaths = adminUserAntPaths;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer securityConfigurer) {
        securityConfigurer.tokenStore(new JwtTokenStore(jwtAccessTokenConverter));
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(STATELESS).and()
                .authorizeRequests(request ->
                        request.antMatchers(anonymousUserAntPaths).access("permitAll")
                                .antMatchers(adminUserAntPaths).access("hasRole('ADMIN')")
                                .antMatchers(authenticatedUserAntPaths).access("authenticated"));
    }
}

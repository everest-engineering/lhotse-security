package engineering.everest.starterkit.security.config;

import engineering.everest.starterkit.security.ApplicationUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


@Configuration
@Order(20)
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private final JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired
    public ResourceServerConfig(JwtAccessTokenConverter jwtAccessTokenConverter,
                                ApplicationUserDetailsService userDetailsService) {
        super();
        var defaultUserAuthenticationConverter = new DefaultUserAuthenticationConverter();
        defaultUserAuthenticationConverter.setUserDetailsService(userDetailsService);
        var accessTokenConverter = (DefaultAccessTokenConverter) jwtAccessTokenConverter.getAccessTokenConverter();
        accessTokenConverter.setUserTokenConverter(defaultUserAuthenticationConverter);
        this.jwtAccessTokenConverter = jwtAccessTokenConverter;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer securityConfigurer) {
        securityConfigurer.tokenStore(new JwtTokenStore(jwtAccessTokenConverter));
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        OrRequestMatcher requestMatcher = new OrRequestMatcher(
                new AndRequestMatcher(
                        new NegatedRequestMatcher(new AntPathRequestMatcher(SecurityConfig.VERSION_API)),
                        new NegatedRequestMatcher(new AntPathRequestMatcher(SecurityConfig.GUEST_API)),
                        new NegatedRequestMatcher(new AntPathRequestMatcher(SecurityConfig.SWAGGER_API_DOCUMENTATION)),
                        new AntPathRequestMatcher(SecurityConfig.APP_API)
                ),
                new AndRequestMatcher(
                        new AntPathRequestMatcher(SecurityConfig.SPRING_ACTUATOR_API),
                        new NegatedRequestMatcher(new AntPathRequestMatcher(SecurityConfig.SPRING_ACTUATOR_HEALTH_API)),
                        new NegatedRequestMatcher(new AntPathRequestMatcher(SecurityConfig.SPRING_ACTUATOR_PROM_API))
                )
        );

        http.cors().and()
                .csrf().disable()
                .requestMatcher(requestMatcher)
                .sessionManagement().sessionCreationPolicy(STATELESS).and()
                .authorizeRequests()
                .anyRequest().authenticated();
    }

}

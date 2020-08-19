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

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


@Configuration
@Order(20)
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private static final String ADMIN_API = "/admin/**";
    private static final String SPRING_ACTUATOR_API = "/actuator/**";
    private static final String APP_API = "/api/**";
    private static final String SPRING_ACTUATOR_PROM_API = "/actuator/prometheus/**";
    private static final String CATCH_ALL_PATH = "/**";
    private static final String ORGANIZATIONS_REGISTER_API = "/api/organizations/register/**";
    private static final String ORGANIZATIONS_REGISTER_CONFIRM_API = "/api/organizations/**/register/**";
    private static final String VERSION_API = "/api/version";
    private static final String SPRING_ACTUATOR_HEALTH_API = "/actuator/health/**";
    private static final String GUEST_API = "/api/guest";
    private static final String SWAGGER_API_DOCUMENTATION = "/api/doc/**";
    private static final String SWAGGER_UI = "/swagger-ui/**";
    private static final String SWAGGER_RESOURCES = "/swagger-resources/**";

    private static final String[] ADMIN_USERS_PATHS = {
            ADMIN_API,
            SPRING_ACTUATOR_API
    };
    private static final String[] AUTHENTICATED_USER_PATHS = {
            APP_API,
            SPRING_ACTUATOR_PROM_API,
            CATCH_ALL_PATH
    };
    private static final String[] ANONYMOUS_USER_PATHS = {
            ORGANIZATIONS_REGISTER_API,
            ORGANIZATIONS_REGISTER_CONFIRM_API,
            VERSION_API,
            SPRING_ACTUATOR_HEALTH_API,
            GUEST_API,
            SWAGGER_API_DOCUMENTATION,
            SWAGGER_UI,
            SWAGGER_RESOURCES
    };

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
        http.cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(STATELESS).and()
                .authorizeRequests(request ->
                        request.antMatchers(ADMIN_USERS_PATHS).access("hasRole('ADMIN')")
                                .antMatchers(ANONYMOUS_USER_PATHS).access("permitAll")
                                .antMatchers(AUTHENTICATED_USER_PATHS).access("authenticated"));
    }
}

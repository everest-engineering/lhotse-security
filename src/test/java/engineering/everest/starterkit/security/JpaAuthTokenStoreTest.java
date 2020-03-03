package engineering.everest.starterkit.security;

import engineering.everest.starterkit.security.persistence.JpaAuthTokenStore;
import engineering.everest.starterkit.security.persistence.OAuth2Serializer;
import engineering.everest.starterkit.security.persistence.OAuthAccessTokenRepository;
import engineering.everest.starterkit.security.persistence.OAuthRefreshTokenRepository;
import engineering.everest.starterkit.security.persistence.PersistableOAuthAccessToken;
import engineering.everest.starterkit.security.persistence.TokenKeyGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaAuthTokenStoreTest {

    private static final String ACCESS_TOKEN_VALUE = "ACCESS_TOKEN_VALUE";
    private static final String ACCESS_TOKEN_KEY = "ACCESS_TOKEN_KEY";
    private static final byte[] AUTHENTICATION_BYTES = "AUTHENTICATION_BYTES".getBytes();

    private JpaAuthTokenStore jpaAuthTokenStore;

    @Mock
    private OAuthAccessTokenRepository oAuthAccessTokenRepository;
    @Mock
    private OAuthRefreshTokenRepository oAuthRefreshTokenRepository;
    @Mock
    private AuthenticationKeyGenerator authenticationKeyGenerator;
    @Mock
    private TokenKeyGenerator tokenKeyGenerator;
    @Mock
    private OAuth2Serializer oAuth2Serializer;

    @BeforeEach
    public void setUp() {
        jpaAuthTokenStore = new JpaAuthTokenStore(oAuthAccessTokenRepository, oAuthRefreshTokenRepository,
                authenticationKeyGenerator, tokenKeyGenerator, oAuth2Serializer);
        when(tokenKeyGenerator.extractKey(ACCESS_TOKEN_VALUE)).thenReturn(ACCESS_TOKEN_KEY);
    }

    @Test
    public void shouldReadAuthentication() {
        PersistableOAuthAccessToken persistableOAuthAccessToken = mock(PersistableOAuthAccessToken.class);
        when(persistableOAuthAccessToken.getAuthentication()).thenReturn(AUTHENTICATION_BYTES);
        when(oAuthAccessTokenRepository.findByTokenId(ACCESS_TOKEN_KEY)).thenReturn(persistableOAuthAccessToken);
        OAuth2Authentication oAuth2Authentication = mock(OAuth2Authentication.class);
        when(oAuth2Serializer.deserializeAuthentication(AUTHENTICATION_BYTES)).thenReturn(oAuth2Authentication);

        assertEquals(oAuth2Authentication, jpaAuthTokenStore.readAuthentication(new DefaultOAuth2AccessToken(ACCESS_TOKEN_VALUE)));
    }
}
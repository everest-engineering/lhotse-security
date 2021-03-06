package engineering.everest.starterkit.security.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OAuthAccessTokenRepository extends JpaRepository<PersistableOAuthAccessToken, UUID> {

    PersistableOAuthAccessToken findByTokenId(String tokenId);

    List<PersistableOAuthAccessToken> findByAuthenticationId(String authenticationId);

    List<PersistableOAuthAccessToken> findByUsernameAndClientId(String username, String clientId);

    List<PersistableOAuthAccessToken> findByClientId(String clientId);

    void deleteByTokenId(String tokenId);

    void deleteByRefreshTokenId(String refreshTokenId);

}

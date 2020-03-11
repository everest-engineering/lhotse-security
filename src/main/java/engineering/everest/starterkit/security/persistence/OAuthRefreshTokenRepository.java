package engineering.everest.starterkit.security.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OAuthRefreshTokenRepository extends JpaRepository<PersistableOAuthRefreshToken, UUID> {

    PersistableOAuthRefreshToken findByTokenId(String tokenId);

    void deleteByTokenId(String tokenId);
}

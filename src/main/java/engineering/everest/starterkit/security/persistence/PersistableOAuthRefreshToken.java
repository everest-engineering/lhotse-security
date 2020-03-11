package engineering.everest.starterkit.security.persistence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "oauthRefreshTokens")
public class PersistableOAuthRefreshToken {

    @Id
    private String tokenId;
    private byte[] token;
    private byte[] authentication;
}

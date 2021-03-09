package engineering.everest.starterkit.security.persistence;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TokenKeyGenerator {

    public String extractKey(String tokenValue) {
        if (tokenValue == null) {
            return null;
        }
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 algorithm not available. Fatal (should be in the JDK).", e);
        }

        byte[] bytes = digest.digest(tokenValue.getBytes(UTF_8));
        return String.format("%032x", new BigInteger(1, bytes));
    }
}

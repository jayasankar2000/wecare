package cfp.wecare.flow.ui.jwt;

import io.jsonwebtoken.Jwts;
import jakarta.xml.bind.DatatypeConverter;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

public class SecretKeyBuilderTest {

    @Test
    void buildSecretKeyJwt() {
        SecretKey key = Jwts.SIG.HS512.key().build();
        String keyString = DatatypeConverter.printHexBinary(key.getEncoded());
        System.out.println(keyString);
    }
}

package joao.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Value("jwt.issuer")
    private String issuer;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expires.in}")
    private Long expiresIn;

    public String getIssuer() {
        return issuer;
    }

    public String getSecret() {
        return secret;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }
}

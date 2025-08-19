package joao.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsJwtSecretConfig {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expires.in}")
    private Long expiresIn;

    @Bean
    public String jwtSecret() { return secret; }

    @Bean
    public String jwtIssuer() { return issuer; }

    @Bean
    public Long jwtExpiresIn() { return expiresIn; }
}

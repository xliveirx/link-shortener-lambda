package joao.config.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JwtSecretPayload {
    @JsonProperty("${jwt.secret}")
    String jwtSecret;

    @JsonProperty("${jwt.issuer}")
    String jwtIssuer;

    @JsonProperty("${jwt.expires.in}")
    Long expiresIn;
}

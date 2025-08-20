package joao.adapter.out;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import joao.config.AwsJwtSecretConfig;
import joao.core.domain.User;
import joao.core.exception.InvalidTokenException;
import joao.core.port.out.JwtServicePortOut;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class JwtServiceAdapterOut implements JwtServicePortOut {

    private final AwsJwtSecretConfig awsJwtSecretConfig;

    public JwtServiceAdapterOut(AwsJwtSecretConfig awsJwtSecretConfig) {
        this.awsJwtSecretConfig = awsJwtSecretConfig;
    }

    @Override
    public String generateToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(awsJwtSecretConfig.jwtSecret());
        try{
            return JWT.create()
                    .withSubject(user.getEmail())
                    .withIssuer(awsJwtSecretConfig.jwtIssuer())
                    .withExpiresAt(expiresAt(awsJwtSecretConfig.jwtExpiresIn()))
                    .sign(algorithm);

        } catch (JWTCreationException ex) {
            throw new InvalidTokenException();
        }
    }

    @Override
    public String validateToken(String token) {
        DecodedJWT decodedJWT;
        try{
            Algorithm algorithm = Algorithm.HMAC256(awsJwtSecretConfig.jwtSecret());
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(awsJwtSecretConfig.jwtIssuer())
                    .build();
            decodedJWT = verifier.verify(token);
            return decodedJWT.getSubject();

        } catch(JWTVerificationException e) {
            throw new InvalidTokenException();
        }
    }

    private Instant expiresAt(Long milis) {
        return Instant.now().plus(Duration.ofMillis(milis));
    }
}

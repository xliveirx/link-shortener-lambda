package joao.adapter.out;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import joao.core.domain.User;
import joao.core.exception.LoginException;
import joao.core.port.out.JwtServicePortOut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class JwtServiceAdapterOut implements JwtServicePortOut {

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public String generateToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        try{
            return JWT.create()
                    .withSubject(user.getEmail())
                    .withIssuer("joao")
                    .withExpiresAt(expiresAt(30))
                    .sign(algorithm);

        } catch (JWTCreationException ex) {
            throw new LoginException();
        }
    }

    @Override
    public String validateToken(String token) {
        DecodedJWT decodedJWT;
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("joao")
                    .build();
            decodedJWT = verifier.verify(token);
            return decodedJWT.getSubject();

        } catch(JwtValidationException e) {
            throw new LoginException();
        }
    }

    private Instant expiresAt(int min) {
        return LocalDateTime.now().plusMinutes(min).toInstant(ZoneOffset.of("-03:00"));
    }
}

package joao.core.port.out;

import joao.core.domain.User;

public interface JwtServicePortOut {

    String generateToken(User user);
    String validateToken(String token);
}


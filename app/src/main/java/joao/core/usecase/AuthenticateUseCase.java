package joao.core.usecase;

import joao.adapter.in.web.dto.LoginRequest;
import joao.adapter.in.web.dto.LoginResponse;
import joao.config.JwtConfig;
import joao.core.exception.LoginException;
import joao.core.port.in.AuthenticatePortIn;
import joao.core.port.out.JwtServicePortOut;
import joao.core.port.out.UserRepositoryPortOut;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticateUseCase implements AuthenticatePortIn {

    private final UserRepositoryPortOut userRepositoryPortOut;
    private final PasswordEncoder passwordEncoder;
    private final JwtServicePortOut jwtServicePortOut;
    private final JwtConfig jwtConfig;

    public AuthenticateUseCase(UserRepositoryPortOut userRepositoryPortOut,
                               PasswordEncoder passwordEncoder,
                               JwtServicePortOut jwtServicePortOut, JwtConfig jwtConfig) {
        this.userRepositoryPortOut = userRepositoryPortOut;
        this.passwordEncoder = passwordEncoder;
        this.jwtServicePortOut = jwtServicePortOut;
        this.jwtConfig = jwtConfig;
    }

    @Override
    public LoginResponse execute(LoginRequest req) {

        var user = this.userRepositoryPortOut.findByEmail(req.email())
                .orElseThrow(LoginException::new);

        var isPasswordValid = passwordEncoder.matches(req.password(), user.getPassword());

        if (!isPasswordValid) {
            throw new LoginException();
        }

        String token = jwtServicePortOut.generateToken(user);

        return new LoginResponse(token, jwtConfig.getExpiresIn()); // 30 min em milissegundos
    }
}
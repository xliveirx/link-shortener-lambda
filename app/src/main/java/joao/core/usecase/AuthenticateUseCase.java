package joao.core.usecase;

import joao.adapter.in.web.dto.LoginRequest;
import joao.adapter.in.web.dto.LoginResponse;
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

    public AuthenticateUseCase(UserRepositoryPortOut userRepositoryPortOut, 
                             PasswordEncoder passwordEncoder,
                             JwtServicePortOut jwtServicePortOut) {
        this.userRepositoryPortOut = userRepositoryPortOut;
        this.passwordEncoder = passwordEncoder;
        this.jwtServicePortOut = jwtServicePortOut;
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
        
        return new LoginResponse(token, 1800000L); // 30 min em milissegundos
    }
}

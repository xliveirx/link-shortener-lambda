package joao.core.usecase;

import joao.core.domain.User;
import joao.core.exception.UserAlreadyExistsException;
import joao.core.port.in.CreateUserPortIn;
import joao.core.port.out.UserRepositoryPortOut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CreateUserUseCase implements CreateUserPortIn {

    private static final Logger logger = LoggerFactory.getLogger(CreateUserUseCase.class);

    private final UserRepositoryPortOut userRepositoryPortOut;
    private final PasswordEncoder passwordEncoder;

    public CreateUserUseCase(UserRepositoryPortOut userRepositoryPortOut, PasswordEncoder passwordEncoder) {
        this.userRepositoryPortOut = userRepositoryPortOut;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User execute(User user) {
        logger.info("Creating user {}", user.getEmail());

        var optUser = userRepositoryPortOut.findByEmail(user.getEmail());

        if(optUser.isPresent()) {
            throw new UserAlreadyExistsException();
        }

        user.encodePassword(passwordEncoder);

        var userCreated = userRepositoryPortOut.save(user);

        logger.info("User created {}", user.getUserId());

        return userCreated;
    }
}

package joao.core.port.out;

import joao.core.domain.User;

import java.util.Optional;

public interface UserRepositoryPortOut {

    User save(User user);

    Optional<User> findByEmail(String email);
}

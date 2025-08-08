package joao.core.port.in;

import joao.core.domain.User;

public interface CreateUserPortIn {

    User execute(User user);
}

package joao.core.port.in;

import joao.adapter.in.web.dto.LoginRequest;
import joao.adapter.in.web.dto.LoginResponse;

public interface AuthenticatePortIn {
    LoginResponse execute(LoginRequest req);
}

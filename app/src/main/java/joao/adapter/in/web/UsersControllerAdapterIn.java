package joao.adapter.in.web;

import joao.adapter.in.web.dto.CreateUserRequest;
import joao.adapter.in.web.dto.CreateUserResponse;
import joao.core.port.in.CreateUserPortIn;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping(path = "/users")
public class UsersControllerAdapterIn {

    private final CreateUserPortIn createUserPortIn;

    public UsersControllerAdapterIn(CreateUserPortIn createUserPortIn) {
        this.createUserPortIn = createUserPortIn;
    }

    @PostMapping
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody CreateUserRequest req){

        var userCreated = createUserPortIn.execute(req.toDomain());

        var body = CreateUserResponse.fromDomain(userCreated);

        return ResponseEntity.created(URI.create("/")).body(body);
    }
}

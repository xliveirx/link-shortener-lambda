package joao.adapter.in.web;

import jakarta.validation.Valid;
import joao.adapter.in.web.dto.CreateUserRequest;
import joao.adapter.in.web.dto.CreateUserResponse;
import joao.core.domain.User;
import joao.core.port.in.CreateUserPortIn;
import joao.core.port.in.DeleteUserPortIn;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(path = "/users")
@Validated
public class UsersControllerAdapterIn {

    private final CreateUserPortIn createUserPortIn;
    private final DeleteUserPortIn deleteUserPortIn;

    public UsersControllerAdapterIn(CreateUserPortIn createUserPortIn, DeleteUserPortIn deleteUserPortIn) {
        this.createUserPortIn = createUserPortIn;
        this.deleteUserPortIn = deleteUserPortIn;
    }

    @PostMapping
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody @Valid CreateUserRequest req){

        var userCreated = createUserPortIn.execute(req.toDomain());

        var body = CreateUserResponse.fromDomain(userCreated);

        return ResponseEntity.created(URI.create("/")).body(body);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal User user){

        var userId = user.getUserId();

        deleteUserPortIn.execute(userId);

        return ResponseEntity.noContent().build();
    }
}

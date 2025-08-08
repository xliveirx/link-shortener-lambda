package joao.adapter.in.web;

import jakarta.validation.Valid;
import joao.adapter.in.web.dto.LoginRequest;
import joao.adapter.in.web.dto.LoginResponse;
import joao.core.port.in.AuthenticatePortIn;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/oauth")
@Validated
public class TokenControllerAdapterIn {

    private final AuthenticatePortIn authenticatePortIn;

    public TokenControllerAdapterIn(AuthenticatePortIn authenticatePortIn) {
        this.authenticatePortIn = authenticatePortIn;
    }

    @PostMapping("/token")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody @Valid LoginRequest request) {
        var response = authenticatePortIn.execute(request);
        return ResponseEntity.ok(response);
    }
}

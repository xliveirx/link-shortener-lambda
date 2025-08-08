package joao.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import joao.core.domain.User;

public record CreateUserRequest(@NotBlank @Size(max = 100) String email,
                                @NotBlank @Size(min = 8, max = 64) String password,
                                @NotBlank @Size(min = 5, max = 50) String nickname) {

    public User toDomain(){
        return new User(email, password, nickname);
    }
}

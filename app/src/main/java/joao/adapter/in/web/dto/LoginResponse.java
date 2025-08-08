package joao.adapter.in.web.dto;

public record LoginResponse(String token, Long expiresIn) {
}

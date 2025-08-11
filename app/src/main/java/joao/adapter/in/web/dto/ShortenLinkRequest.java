package joao.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import joao.core.domain.Link;
import joao.core.domain.User;

import java.time.LocalDateTime;
import java.util.UUID;

public record ShortenLinkRequest(
        @NotBlank String uniqueLinkSlug,
        @NotBlank String originalUrl,
        UtmTagsRequest utm,
        LocalDateTime expirationDateTime
) {

    public Link toDomain(UUID userId){
        return new Link(
                uniqueLinkSlug,
                originalUrl,
                utm != null ? utm.toDomain() : null,
                new User(userId),
                true,
                expirationDateTime,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}

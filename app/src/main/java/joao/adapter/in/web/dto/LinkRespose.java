package joao.adapter.in.web.dto;

import joao.core.domain.Link;

import java.time.LocalDateTime;

public record LinkRespose (
        String linkId,
        String originalUrl,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {

    public static LinkRespose fromDomain(Link link) {
        return new LinkRespose(
                link.getLinkId(),
                link.getOriginalUrl(),
                link.isActive(),
                link.getCreatedAt(),
                link.getUpdatedAt()
        );
    }
}

package joao.adapter.in.web.dto;

import joao.core.domain.UtmTags;

public record UtmTagsRequest(String source, String medium, String campaign, String content) {

    public UtmTags toDomain() {
        return new UtmTags(
                this.source,
                this.medium,
                this.campaign,
                this.content
        );
    }
}

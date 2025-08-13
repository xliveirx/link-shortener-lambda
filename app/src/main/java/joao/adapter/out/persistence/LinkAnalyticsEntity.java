package joao.adapter.out.persistence;

import joao.config.TableName;
import joao.core.domain.Link;
import joao.core.domain.LinkAnalytics;
import software.amazon.awssdk.enhanced.dynamodb.extensions.annotations.DynamoDbAtomicCounter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@DynamoDbBean
@TableName(name = "tb_links_analytics")
public class LinkAnalyticsEntity {

    private String linkId;
    private LocalDate date;
    private Integer clicks;
    private Instant updatedAt;

    public LinkAnalyticsEntity() {}

    public static LinkAnalyticsEntity fromDomain(Link link, LocalDate date) {
        var entity = new LinkAnalyticsEntity();
        entity.linkId = link.getLinkId();
        entity.date = date;
        entity.clicks = 1;
        entity.updatedAt = Instant.now();

        return entity;
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("link_id")
    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("date")
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @DynamoDbAtomicCounter(startValue = 1)
    @DynamoDbAttribute("clicks")
    public Integer getClicks() {
        return clicks;
    }

    public void setClicks(Integer clicks) {
        this.clicks = clicks;
    }

    @DynamoDbAttribute("updated_at")
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LinkAnalytics toDomain() {
        return new LinkAnalytics(
                linkId,
                date,
                Long.valueOf(clicks),
                LocalDateTime.ofInstant(updatedAt, ZoneId.systemDefault())
        );
    }
}

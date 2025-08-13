package joao.adapter.out.persistence;

import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import joao.core.domain.Link;
import joao.core.domain.LinkAnalytics;
import joao.core.port.out.AnalyticsRepositoryPortOut;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static joao.adapter.out.persistence.DynamoAttributeConstants.*;

@Component
public class AnalyticsDynamoDbAdapterOut implements AnalyticsRepositoryPortOut {

    private final DynamoDbTemplate dynamoDbTemplate;
    private final DynamoDbClient dynamoDbClient;

    public AnalyticsDynamoDbAdapterOut(DynamoDbTemplate dynamoDbTemplate, DynamoDbClient dynamoDbClient) {
        this.dynamoDbTemplate = dynamoDbTemplate;
        this.dynamoDbClient = dynamoDbClient;
    }

    @Override
    public void updateClickCount(Link link) {

        var date = LocalDate.now();

        var key = Key.builder()
                .partitionValue(link.getLinkId())
                .sortValue(date.toString())
                .build();

        var entity = dynamoDbTemplate.load(key, LinkAnalyticsEntity.class);

        if (entity != null){

            updateAnalytics(entity, date);

        } else {

            dynamoDbTemplate.save(LinkAnalyticsEntity.fromDomain(link, date));

        }
    }

    private void updateAnalytics(LinkAnalyticsEntity entity,
                                 LocalDate date) {

        Map<String, AttributeValue> key = Map.of(
                ANALYTICS_LINK_ID, AttributeValue.fromS(entity.getLinkId()),
                ANALYTICS_DATE, AttributeValue.fromS(date.toString())
        );

        Map<String, AttributeValue> expressionValues = Map.of(
                ":zero", AttributeValue.fromN("0"),
                ":inc", AttributeValue.fromN("1"),
                ":now", AttributeValue.fromS(Instant.now().toString())
        );

        UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName("tb_links_analytics")
                .key(key)
                .updateExpression(format("SET %s = if_not_exists(%s, :zero) + :inc, updated_at = :now", ANALYTICS_CLICKS, ANALYTICS_CLICKS))
                .expressionAttributeValues(expressionValues)
                .build();

        dynamoDbClient.updateItem(request);
    }


    @Override
    public List<LinkAnalytics> findAll(String linkId, LocalDate startDate, LocalDate endDate) {

        var conditional = QueryConditional.sortBetween(
                Key.builder()
                        .partitionValue(linkId)
                        .sortValue(startDate.toString())
                        .build(),
                Key.builder()
                        .partitionValue(linkId)
                        .sortValue(endDate.toString())
                        .build()
        );

        return dynamoDbTemplate.query(QueryEnhancedRequest.builder()
                        .queryConditional(conditional)
                        .build(), LinkAnalyticsEntity.class)
                .items()
                .stream()
                .map(LinkAnalyticsEntity::toDomain)
                .toList();
    }

}

package joao.adapter.out.persistence;

import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import joao.adapter.out.persistence.helper.LinkDynamoDbTokenHelper;
import joao.core.domain.Link;
import joao.core.domain.LinkFilter;
import joao.core.domain.PaginatedResult;
import joao.core.port.out.LinkRepositoryPortOut;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static joao.config.Constants.FK_TB_USERS_LINK_USER_INDEX;

@Component
public class LinkDynamoDbAdapterOut implements LinkRepositoryPortOut {

    private final DynamoDbTemplate dynamoDbTemplate;
    private final LinkDynamoDbTokenHelper dynamoTokenHelper;

    public LinkDynamoDbAdapterOut(DynamoDbTemplate dynamoDbTemplate, LinkDynamoDbTokenHelper dynamoTokenHelper) {
        this.dynamoDbTemplate = dynamoDbTemplate;
        this.dynamoTokenHelper = dynamoTokenHelper;
    }

    @Override
    public Link save(Link link) {

        var entity = LinkEntity.fromDomain(link);

        dynamoDbTemplate.save(entity);

        return link;
    }

    @Override
    public Optional<Link> findById(String id) {

        var key = Key.builder()
                .partitionValue(id)
                .build();

        var entity = dynamoDbTemplate.load(key, LinkEntity.class);

        return entity == null ?
                Optional.empty() :
                Optional.of(entity.toDomain());
    }

    @Override
    public PaginatedResult<Link> findAllByUserId(String userId, String nextToken, int limit, LinkFilter filters) {

        var qc = QueryConditional.keyEqualTo(
                Key.builder()
                        .partitionValue(userId.toString())
                        .build()
        );

        List<String> conditions = new ArrayList<>();
        Map<String, AttributeValue> expValues = new HashMap<>();

        if(filters.active() != null) {
            conditions.add("active = :activeValue");
            expValues.put(":activeValue", AttributeValue.fromBool(filters.active()));
        }

        if(filters.startCreatedAt() != null && filters.endCreatedAt() != null) {
            conditions.add("created_at BETWEEN :startCreatedAt AND :endCreatedAt");
            expValues.put(":startCreatedAt", AttributeValue.fromS(LocalDateTime.of(filters.startCreatedAt(), LocalTime.MIN).toString()));
            expValues.put(":endCreatedAt", AttributeValue.fromS(LocalDateTime.of(filters.endCreatedAt(), LocalTime.MAX).toString()));
        }

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(qc)
                .limit(limit);

        if (!conditions.isEmpty() ) {
            requestBuilder.filterExpression(Expression.builder()
                    .expression(String.join(" AND ", conditions))
                    .expressionValues(expValues)
                    .build());
        }

        if(nextToken != null && !nextToken.isEmpty()) {
            var map = dynamoTokenHelper.decodeStartToken(nextToken);
            requestBuilder.exclusiveStartKey(map);
        }

        Page<LinkEntity> page = dynamoDbTemplate
                .query(requestBuilder.build(), LinkEntity.class, FK_TB_USERS_LINK_USER_INDEX)
                .stream()
                .findFirst()
                .orElse(null);

        if(page == null) {
            return new PaginatedResult<>(Collections.emptyList(), null, false);
        }

        var links = page.items()
                .stream()
                .map(LinkEntity::toDomain)
                .collect(Collectors.toList());

        return new PaginatedResult<>(
                links,
                page.lastEvaluatedKey() != null ? dynamoTokenHelper.encodeStartToken(page.lastEvaluatedKey()) : null,
                page.lastEvaluatedKey() != null
        );

    }
}

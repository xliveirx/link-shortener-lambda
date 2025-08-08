package joao.adapter.out.persistence;

import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import joao.core.domain.User;
import joao.core.port.out.UserRepositoryPortOut;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Optional;

import static joao.config.Constants.EMAIL_INDEX;

@Component
public class UserDynamoDbAdapterOut implements UserRepositoryPortOut {

    private final DynamoDbTemplate dynamoDbTemplate;

    public UserDynamoDbAdapterOut(DynamoDbTemplate dynamoDbTemplate) {
        this.dynamoDbTemplate = dynamoDbTemplate;
    }

    @Override
    public User save(User user) {
        var entity = UserEntity.fromDomain(user);

        dynamoDbTemplate.save(entity);

        return user;
    }

    @Override
    public Optional<User> findByEmail(String email) {

        var cond = QueryConditional.keyEqualTo(k ->
                k.partitionValue(AttributeValue.builder().s(email).build()));

        var query = QueryEnhancedRequest.builder()
                .queryConditional(cond)
                .build();

        var result = dynamoDbTemplate.query(query, UserEntity.class, EMAIL_INDEX);

        return result.stream()
                .flatMap(userEntityPage -> userEntityPage.items().stream())
                .map(UserEntity::toDomain)
                .findFirst();
    }
}

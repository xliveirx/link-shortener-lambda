package joao.adapter.out.persistence;

import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import joao.core.domain.User;
import joao.core.port.out.UserRepositoryPortOut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Optional;
import java.util.UUID;

import static joao.config.Constants.EMAIL_INDEX;

@Component
public class UserDynamoDbAdapterOut implements UserRepositoryPortOut {

    private final Logger logger = LoggerFactory.getLogger(UserDynamoDbAdapterOut.class);
    private final DynamoDbTemplate dynamoDbTemplate;

    public UserDynamoDbAdapterOut(DynamoDbTemplate dynamoDbTemplate) {
        this.dynamoDbTemplate = dynamoDbTemplate;
    }

    @Override
    public User save(User user) {

        logger.debug("Executing save User on Dynamodb - {}", user);

        var entity = UserEntity.fromDomain(user);

        dynamoDbTemplate.save(entity);

        logger.debug("User saved into DynamoDb - {}", user);

        return user;
    }

    @Override
    public Optional<User> findByEmail(String email) {

        logger.debug("Finding user by email on DynamoDb... - {}", email);

        var cond = QueryConditional.keyEqualTo(k ->
                k.partitionValue(AttributeValue.builder().s(email).build()));

        var query = QueryEnhancedRequest.builder()
                .queryConditional(cond)
                .build();

        var result = dynamoDbTemplate.query(query, UserEntity.class, EMAIL_INDEX);

        var opt = result.stream()
                .flatMap(userEntityPage -> userEntityPage.items().stream())
                .map(UserEntity::toDomain)
                .findFirst();

        logger.debug("User found by email on DynamoDb - {}", email);

        return opt;
    }

    @Override
    public void deleteById(UUID userId) {

        logger.debug("Executing delete User on DynamoDb - {}", userId);

        var key = Key.builder().partitionValue(userId.toString())
                .build();

        dynamoDbTemplate.delete(key, UserEntity.class);

        logger.debug("User deleted on DynamoDb - {}", userId);
    }

    @Override
    public Optional<User> findById(UUID userId) {

        logger.debug("Executing find User by Id on DynamoDb - {}", userId);

        var key = Key.builder().partitionValue(userId.toString())
                .build();

        var entity = dynamoDbTemplate.load(key, UserEntity.class);

        return entity == null ?
                Optional.empty() :
                Optional.of(entity.toDomain());
    }
}

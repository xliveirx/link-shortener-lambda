package joao.adapter.out.persistence;

import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import joao.core.domain.User;
import joao.core.port.out.UserRepositoryPortOut;
import org.springframework.stereotype.Component;

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
}

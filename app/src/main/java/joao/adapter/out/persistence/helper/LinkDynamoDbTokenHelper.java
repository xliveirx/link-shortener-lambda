package joao.adapter.out.persistence.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import static joao.adapter.out.persistence.DynamoAttributeConstants.LINK_ID;
import static joao.adapter.out.persistence.DynamoAttributeConstants.LINK_USER_ID;

@Component
public class LinkDynamoDbTokenHelper {

    private final ObjectMapper mapper;

    public LinkDynamoDbTokenHelper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public String encodeStartToken(Map<String, AttributeValue> key){
        try {
            if (key == null || key.isEmpty() || !key.containsKey(LINK_ID) || !key.containsKey(LINK_USER_ID)) {
                System.err.println("Invalid key for token encoding: " + key);
                return null;
            }

            AttributeValue linkIdValue = key.get(LINK_ID);
            AttributeValue userIdValue = key.get(LINK_USER_ID);

            if (linkIdValue == null || userIdValue == null || 
                linkIdValue.s() == null || userIdValue.s() == null ||
                linkIdValue.s().isEmpty() || userIdValue.s().isEmpty()) {
                System.err.println("Invalid values in key for token encoding: linkId=" + 
                                  (linkIdValue != null ? linkIdValue.s() : "null") + 
                                  ", userId=" + (userIdValue != null ? userIdValue.s() : "null"));
                return null;
            }

            var dto = new TokenDto(linkIdValue.s(), userIdValue.s());
            String json = mapper.writeValueAsString(dto);

            String token = Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
            System.out.println("Encoded token: " + token + " for linkId=" + linkIdValue.s() + ", userId=" + userIdValue.s());

            return token;
        } catch (Exception ex) {
            System.err.println("Error encoding token: " + ex.getMessage());
            throw new RuntimeException("Failed to encode start token", ex);
        }
    }

    public Map<String, AttributeValue> decodeStartToken(String token){
        try {
            if (token == null || token.isEmpty()) {
                throw new IllegalArgumentException("Token cannot be null or empty");
            }

            byte[] decoded;
            try {
                decoded = Base64.getDecoder().decode(token);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid Base64 token format: " + e.getMessage(), e);
            }

            String json = new String(decoded, StandardCharsets.UTF_8);

            var dto = mapper.readValue(json, TokenDto.class);

            return Map.of(
                    LINK_USER_ID, AttributeValue.fromS(dto.userId()),
                    LINK_ID, AttributeValue.fromS(dto.linkId())
            );

        } catch (Exception ex) {
            System.err.println("Error decoding token: " + token + ", Error: " + ex.getMessage());
            throw new RuntimeException("Failed to decode start token", ex);
        }
    }
}

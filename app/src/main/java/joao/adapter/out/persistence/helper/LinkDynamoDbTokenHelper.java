package joao.adapter.out.persistence.helper;

import com.fasterxml.jackson.databind.JsonNode;
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

    private static final String SEP = "|";
    private final ObjectMapper mapper;

    public LinkDynamoDbTokenHelper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public String encodeStartToken(Map<String, AttributeValue> key) {
        if (key == null || key.isEmpty()) return null;

        String userId = getS(key.get(LINK_USER_ID));
        String linkId = getS(key.get(LINK_ID));
        if (userId == null || linkId == null) return null;

        String raw = userId + SEP + linkId; // new simple format
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(raw.getBytes(StandardCharsets.UTF_8));
    }

    public Map<String, AttributeValue> decodeStartToken(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }

        String raw = decodeBase64Lenient(token);

        int sep = raw.indexOf(SEP);
        if (sep > 0 && sep < raw.length() - 1) {
            String userId = raw.substring(0, sep);
            String linkId = raw.substring(sep + 1);
            return Map.of(
                    LINK_USER_ID, AttributeValue.fromS(userId),
                    LINK_ID, AttributeValue.fromS(linkId)
            );
        }

        String trimmed = raw.trim();
        if (trimmed.startsWith("{")) {
            try {
                JsonNode n = mapper.readTree(trimmed);
                String userId = textOrNull(n.get("userId"));
                String linkId = textOrNull(n.get("linkId"));
                if (isBlank(userId) || isBlank(linkId)) {
                    throw new IllegalArgumentException("Token JSON missing userId/linkId");
                }
                return Map.of(
                        LINK_USER_ID, AttributeValue.fromS(userId),
                        LINK_ID, AttributeValue.fromS(linkId)
                );
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid JSON token", e);
            }
        }

        throw new IllegalArgumentException("Unrecognized token format");
    }

    private static String decodeBase64Lenient(String token) {
        try {
            return new String(Base64.getUrlDecoder().decode(token), StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e1) {
            try {
                return new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8);
            } catch (IllegalArgumentException e2) {
                throw new IllegalArgumentException("Invalid Base64 token", e2);
            }
        }
    }

    private static String getS(AttributeValue av) {
        return (av != null && av.s() != null && !av.s().isEmpty()) ? av.s() : null;
    }

    private static String textOrNull(JsonNode node) {
        return (node == null || node.isNull()) ? null : node.asText(null);
    }

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
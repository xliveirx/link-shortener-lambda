package joao.core.domain;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LinkTest {

    @Nested
    class generateFullUrl {

        @Test
        void shouldGenerateFullUrlWhenThereIsNoUtmTags() {

            // Arrange
            String originalUrl = "https://google.com";
            UtmTags utmTags = new UtmTags(null, null, null, null);

            Link link = new Link(
                    UUID.randomUUID().toString(),
                    originalUrl,
                    utmTags,
                    null,
                    false,
                    null,
                    LocalDateTime.now(),
                    LocalDateTime.now()
                    );

            // Act
            var fullUrl = link.generateFullUrl();

            // Assert
            assertEquals(originalUrl, fullUrl);
        }


        @Test
        void shouldGenerateFullUrlWhenThereIsUtmTags() {

            // Arrange
            String originalUrl = "https://google.com";
            UtmTags utmTags = new UtmTags("test", "test", "test", "test");

            Link link = new Link(
                    UUID.randomUUID().toString(),
                    originalUrl,
                    utmTags,
                    null,
                    false,
                    null,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );

            // Act
            var fullUrl = link.generateFullUrl();

            var expectedUrl = originalUrl + "?utm_source=test&utm_medium=test&utm_campaign=test&utm_content=test";

            // Assert
            assertEquals(expectedUrl , fullUrl);

        }
    }


}
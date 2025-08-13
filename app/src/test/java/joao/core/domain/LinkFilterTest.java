package joao.core.domain;

import joao.core.exception.FilterException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class LinkFilterTest {


    @Nested
    class validate {

        @Test
        void shouldNotThrowFilterExceptionWhenStartCreatedAtIsNull() {

            // Arrange
            LocalDate endCreatedAt = LocalDate.now();

            // Act & Assert
            LinkFilter linkFilter = new LinkFilter(false, null, endCreatedAt);
            assertDoesNotThrow(linkFilter::validate);
        }

        @Test
        void shouldNotThrowFilterExceptionWhenEndCreatedAtIsNull() {

            // Arrange
            LocalDate startCreatedAt = LocalDate.now();

            // Act & Assert
            LinkFilter linkFilter = new LinkFilter(false, startCreatedAt, null);
            assertDoesNotThrow(linkFilter::validate);

        }

        @Test
        void shouldNotThrowFilterExceptionWhenBothAreNull() {

            // Act & Assert
            LinkFilter linkFilter = new LinkFilter(false, null, null);
            assertDoesNotThrow(linkFilter::validate);

        }

        @Test
        void shouldThrowFilterExceptionWhenStartCreatedAtIsAfterEndCreatedAt() {

            // Arrange
            LocalDate startCreatedAt = LocalDate.now().plusDays(4);
            LocalDate endCreatedAt = LocalDate.now();

            // Act & Assert
            LinkFilter linkFilter = new LinkFilter(false, startCreatedAt, endCreatedAt);
            assertThrows(FilterException.class,
                    linkFilter::validate);
        }
    }

}
package joao.core.usecase;

import joao.core.domain.Link;
import joao.core.domain.LinkFilter;
import joao.core.domain.PaginatedResult;
import joao.core.exception.FilterException;
import joao.core.port.out.LinkRepositoryPortOut;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserLinksUseCaseTest {

    @Mock
    LinkFilter linkFilter;

    @Mock
    LinkRepositoryPortOut linkRepositoryPortOut;

    @InjectMocks
    UserLinksUseCase userLinksUseCase;

    @Nested
    class execute {

        @Test
        void shouldCallFindAllByUserId() {

            // Arrange
            PaginatedResult<Link> userLinks = new PaginatedResult<>(new ArrayList<>(), null, false);
            String userId = UUID.randomUUID().toString();
            String nextToken = "dajendjnadnas";
            int limit = 10;
            doNothing().when(linkFilter).validate();
            doReturn(userLinks).when(linkRepositoryPortOut).findAllByUserId(userId, nextToken, limit, linkFilter);

            // Act
            var output = userLinksUseCase.execute(userId, nextToken, limit, linkFilter);

            // Assert
            verify(linkRepositoryPortOut, times(1))
                    .findAllByUserId(userId, nextToken, limit, linkFilter);

            assertEquals(userLinks, output);
        }

        @Test
        void shouldNotCallFindAllByUserIdWhenFilterException() {

            // Arrange
            String userId = UUID.randomUUID().toString();
            String nextToken = "dajendjnadnas";
            int limit = 10;
            doThrow(FilterException.class).when(linkFilter).validate();

            // Act & Assert
            assertThrows(FilterException.class,
                    () -> userLinksUseCase.execute(userId, nextToken, limit, linkFilter));

            // Assert
            verify(linkRepositoryPortOut, times(0))
                    .findAllByUserId(anyString(), anyString(), anyInt(), any(LinkFilter.class));

        }
    }
}
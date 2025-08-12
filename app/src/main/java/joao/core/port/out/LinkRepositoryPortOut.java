package joao.core.port.out;

import joao.core.domain.Link;
import joao.core.domain.PaginatedResult;

import java.util.Optional;

public interface LinkRepositoryPortOut {
    Link save(Link link);
    Optional<Link> findById(String id);
    PaginatedResult<Link> findAllByUserId(String userId, String nextToken, int limit);
}

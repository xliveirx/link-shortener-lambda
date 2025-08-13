package joao.core.port.in;

import joao.core.domain.Link;
import joao.core.domain.LinkFilter;
import joao.core.domain.PaginatedResult;

public interface UserLinksPortIn {

    PaginatedResult<Link> execute(
            String uuid,
            String nextToken,
            int limit,
            LinkFilter filters);
}

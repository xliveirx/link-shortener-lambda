package joao.core.usecase;

import joao.core.domain.Link;
import joao.core.domain.PaginatedResult;
import joao.core.port.in.MyLinksPortIn;
import joao.core.port.out.LinkRepositoryPortOut;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MyLinksUseCase implements MyLinksPortIn {

    private final LinkRepositoryPortOut linkRepositoryPortOut;

    public MyLinksUseCase(LinkRepositoryPortOut linkRepositoryPortOut) {
        this.linkRepositoryPortOut = linkRepositoryPortOut;
    }

    @Override
    public PaginatedResult<Link> execute(String userId, String nextToken, int limit) {

        return linkRepositoryPortOut.findAllByUserId(userId, nextToken, limit);

    }
}

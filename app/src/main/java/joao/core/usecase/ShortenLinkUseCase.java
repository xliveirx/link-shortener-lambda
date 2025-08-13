package joao.core.usecase;

import joao.core.domain.Link;
import joao.core.exception.LinkAlreadyExistsException;
import joao.core.port.in.ShortenLinkPortIn;
import joao.core.port.out.LinkRepositoryPortOut;
import org.springframework.stereotype.Component;

@Component
public class ShortenLinkUseCase implements ShortenLinkPortIn {

    private final LinkRepositoryPortOut linkRepositoryPortOut;

    public ShortenLinkUseCase(LinkRepositoryPortOut linkRepositoryPortOut) {
        this.linkRepositoryPortOut = linkRepositoryPortOut;
    }

    @Override
    public String execute(Link link) {

        var optLink = linkRepositoryPortOut.findById(link.getLinkId());

        if(optLink.isPresent()) {
            throw new LinkAlreadyExistsException();
        }

        linkRepositoryPortOut.save(link);

        return link.getLinkId();
    }
}

package joao.core.usecase;

import joao.core.exception.LinkNotFoundException;
import joao.core.port.in.RedirectPortIn;
import joao.core.port.out.LinkRepositoryPortOut;
import org.springframework.stereotype.Component;

@Component
public class RedirectUseCase implements RedirectPortIn {

    private final LinkRepositoryPortOut linkRepositoryPortOut;

    public RedirectUseCase(LinkRepositoryPortOut linkRepositoryPortOut) {
        this.linkRepositoryPortOut = linkRepositoryPortOut;
    }

    @Override
    public String execute(String linkId) {

        var link = linkRepositoryPortOut.findById(linkId)
                .orElseThrow(LinkNotFoundException::new);

        return link.generateFullUrl();
    }
}

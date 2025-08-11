package joao.core.usecase;

import joao.adapter.out.persistence.AnalyticsDynamoDbAdapterOut;
import joao.core.exception.LinkExpiredException;
import joao.core.exception.LinkNotFoundException;
import joao.core.port.in.RedirectPortIn;
import joao.core.port.out.LinkRepositoryPortOut;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RedirectUseCase implements RedirectPortIn {

    private final LinkRepositoryPortOut linkRepositoryPortOut;
    private final AnalyticsDynamoDbAdapterOut analyticsDynamoDbAdapterOut;

    public RedirectUseCase(LinkRepositoryPortOut linkRepositoryPortOut, AnalyticsDynamoDbAdapterOut analyticsDynamoDbAdapterOut) {
        this.linkRepositoryPortOut = linkRepositoryPortOut;
        this.analyticsDynamoDbAdapterOut = analyticsDynamoDbAdapterOut;
    }

    @Override
    public String execute(String linkId) {

        var link = linkRepositoryPortOut.findById(linkId)
                .orElseThrow(LinkNotFoundException::new);

        if(!link.isActive()){
            throw new LinkNotFoundException();
        }

        if(link.getExpirationDateTime().isBefore(LocalDateTime.now())){
            throw new LinkExpiredException();
        }

        analyticsDynamoDbAdapterOut.updateClickCount(link);

        return link.generateFullUrl();
    }
}

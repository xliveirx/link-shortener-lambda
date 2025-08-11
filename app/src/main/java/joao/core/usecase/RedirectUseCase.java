package joao.core.usecase;

import joao.adapter.out.persistence.AnalyticsDynamoDbAdapterOut;
import joao.core.exception.LinkNotFoundException;
import joao.core.port.in.RedirectPortIn;
import joao.core.port.out.LinkRepositoryPortOut;
import org.springframework.stereotype.Component;

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

        analyticsDynamoDbAdapterOut.updateClickCount(link);

        return link.generateFullUrl();
    }
}

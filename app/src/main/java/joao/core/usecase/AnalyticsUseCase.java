package joao.core.usecase;

import joao.adapter.in.web.dto.AnalyticsDataResponse;
import joao.adapter.in.web.dto.AnalyticsResponse;
import joao.core.domain.LinkAnalytics;
import joao.core.exception.FilterException;
import joao.core.exception.LinkNotAllowedException;
import joao.core.exception.LinkNotFoundException;
import joao.core.port.in.AnalyticsPortIn;
import joao.core.port.out.AnalyticsRepositoryPortOut;
import joao.core.port.out.LinkRepositoryPortOut;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class AnalyticsUseCase implements AnalyticsPortIn {

    private final LinkRepositoryPortOut linkRepositoryPortOut;
    private final AnalyticsRepositoryPortOut analyticsRepositoryPortOut;

    public AnalyticsUseCase(LinkRepositoryPortOut linkRepositoryPortOut, AnalyticsRepositoryPortOut analyticsRepositoryPortOut) {
        this.linkRepositoryPortOut = linkRepositoryPortOut;
        this.analyticsRepositoryPortOut = analyticsRepositoryPortOut;
    }

    @Override
    public AnalyticsResponse execute(String userId, String linkId, LocalDate startDate, LocalDate endDate) {

        validateRange(startDate, endDate);

        var link = linkRepositoryPortOut.findById(linkId)
                .orElseThrow(LinkNotFoundException::new);

        if(!link.isFromUser(UUID.fromString(userId))) {
            throw new LinkNotAllowedException();
        }

        var linkAnalytics = analyticsRepositoryPortOut.findAll(linkId, startDate, endDate);

        var totalVisitors = getTotalVisitors(linkAnalytics);

        var analyticsPerDay = getAnalyticsPerDat(linkAnalytics);

        return new AnalyticsResponse(totalVisitors,
                analyticsPerDay);
    }

    private void validateRange(LocalDate startDate, LocalDate endDate) {
        if(startDate.isAfter(endDate)) {
            throw new FilterException(Map.of("startDate", "must be before endDate"));
        }
    }

    private List<AnalyticsDataResponse> getAnalyticsPerDat(List<LinkAnalytics> linkAnalytics) {

        return linkAnalytics.stream()
                .map(AnalyticsDataResponse::fromDomain)
                .toList();
    }

    private Long getTotalVisitors(List<LinkAnalytics> linkAnalytics) {

        return linkAnalytics.stream()
                .map(LinkAnalytics::getClicks)
                .reduce(0L, Long::sum);
    }
}

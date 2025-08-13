package joao.adapter.in.web.dto;

import joao.core.domain.LinkAnalytics;

import java.time.LocalDate;

public record AnalyticsDataResponse(LocalDate date, Long totalVisitors) {

    public static AnalyticsDataResponse fromDomain(LinkAnalytics linkAnalytics) {
        return new AnalyticsDataResponse(linkAnalytics.getDate(), linkAnalytics.getClicks());
    }
}

package joao.adapter.in.web.dto;

import java.util.List;

public record AnalyticsResponse(Long totalVisitors,
                                List<AnalyticsDataResponse> analytics) {
}

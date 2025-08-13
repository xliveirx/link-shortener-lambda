package joao.core.port.in;

import joao.adapter.in.web.dto.AnalyticsResponse;

import java.time.LocalDate;

public interface AnalyticsPortIn {

    AnalyticsResponse execute(String userId,
                              String linkId,
                              LocalDate startDate,
                              LocalDate endDate);
}

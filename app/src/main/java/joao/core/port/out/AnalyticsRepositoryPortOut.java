package joao.core.port.out;

import joao.core.domain.Link;
import joao.core.domain.LinkAnalytics;

import java.time.LocalDate;
import java.util.List;

public interface AnalyticsRepositoryPortOut {

    void updateClickCount(Link link);
    List<LinkAnalytics> findAll(String linkId, LocalDate startDate, LocalDate endDate);
}

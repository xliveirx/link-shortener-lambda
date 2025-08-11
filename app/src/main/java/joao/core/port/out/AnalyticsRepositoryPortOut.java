package joao.core.port.out;

import joao.core.domain.Link;

public interface AnalyticsRepositoryPortOut {

    void updateClickCount(Link link);
}

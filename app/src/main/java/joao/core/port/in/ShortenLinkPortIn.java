package joao.core.port.in;

import joao.adapter.in.web.dto.ShortenLinkResponse;
import joao.core.domain.Link;

public interface ShortenLinkPortIn {
    String execute(Link req);
}

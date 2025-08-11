package joao.core.port.out;

import joao.core.domain.Link;

import java.util.Optional;

public interface LinkRepositoryPortOut {
    Link save(Link link);
    Optional<Link> findById(String id);
}

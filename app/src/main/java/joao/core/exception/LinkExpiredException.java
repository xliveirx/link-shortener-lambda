package joao.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class LinkExpiredException extends DomainException {
    @Override
    public ProblemDetail toProblemDetail() {

        var pb = ProblemDetail.forStatus(HttpStatus.GONE);

        pb.setTitle("Link expired");
        pb.setDetail("The link has expired");

        return pb;
    }
}

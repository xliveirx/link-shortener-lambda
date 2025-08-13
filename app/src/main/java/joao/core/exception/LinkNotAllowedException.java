package joao.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class LinkNotAllowedException extends DomainException {

    @Override
    public ProblemDetail toProblemDetail() {

        var pb = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);

        pb.setTitle("Link Not Allowed");
        pb.setDetail("You are not allowed to access this link.");

        return pb;
    }
}

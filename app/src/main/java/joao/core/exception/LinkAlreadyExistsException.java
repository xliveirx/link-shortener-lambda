package joao.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class LinkAlreadyExistsException extends DomainException {

    @Override
    public ProblemDetail toProblemDetail() {

        var pb = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);

        pb.setTitle("Link Already Exists Exception");
        pb.setDetail("There is already a shorten linl with the same original url. Try a different one.");

        return pb;
    }
}

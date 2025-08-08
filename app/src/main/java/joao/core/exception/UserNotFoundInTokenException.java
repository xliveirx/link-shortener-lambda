package joao.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class UserNotFoundInTokenException extends DomainException {

    @Override
    public ProblemDetail toProblemDetail() {
        var pb = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);

        pb.setTitle("User Not Found In Token Exception");
        pb.setDetail("The user associated with this token no longer exists.");

        return pb;
    }
}

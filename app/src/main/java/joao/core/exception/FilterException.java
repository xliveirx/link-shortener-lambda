package joao.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.util.Map;

public class FilterException extends DomainException {

    private Map<String, Object> invalidFields;

    public FilterException(Map<String, Object> invalidField) {
        this.invalidFields = invalidField;
    }

    @Override
    public ProblemDetail toProblemDetail() {

        var pb = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        pb.setTitle("There was a problem with the filter request.");
        pb.setDetail("Bad Request");

        pb.setProperties(Map.of("validations", invalidFields));

        return pb;
    }
}

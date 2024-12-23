package cfp.wecare.flow.ui.publicAccess.Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class PublicAccessException extends RuntimeException {
    HttpStatus httpStatus;
    Object error;

    public PublicAccessException(HttpStatus httpStatus, String msg) {
        super(msg);
        this.httpStatus = httpStatus;
    }

    public PublicAccessException(HttpStatus httpStatus, Exception ex) {
        super(ex);
        this.httpStatus = httpStatus;
    }

    public PublicAccessException(String msg, Exception ex) {
        super(ex);
        this.error = ex;
    }
}

package cfp.wecare.flow.ui.org.Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class OrgException extends RuntimeException {
    HttpStatus httpStatus;
    Object error;

    OrgException() {

    }

    public OrgException(HttpStatus httpStatus, String msg) {
        super(msg);
        this.httpStatus = httpStatus;
    }

    public OrgException(HttpStatus httpStatus, Exception ex) {
        super(ex);
        this.httpStatus = httpStatus;
    }

    public OrgException(String msg, Exception ex) {
        super(ex);
        this.error = ex;
    }
}

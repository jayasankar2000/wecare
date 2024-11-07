package cfp.wecare.flow.ui.item.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ItemException extends RuntimeException {

    private HttpStatus httpStatus;
    private Object error;

    public ItemException() {
    }

    public ItemException(HttpStatus httpStatus, String msg) {
        super(msg);
        this.httpStatus = httpStatus;
    }

    public ItemException(HttpStatus httpStatus, Exception ex) {
        super(ex);
        this.httpStatus = httpStatus;
        this.error = ex;
    }

    public ItemException(String msg, Exception ex) {
        super(ex);
        this.error = ex;
    }
}

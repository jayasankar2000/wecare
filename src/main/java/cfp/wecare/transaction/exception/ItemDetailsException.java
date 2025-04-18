package cfp.wecare.transaction.exception;

import org.springframework.http.HttpStatus;

public class ItemDetailsException extends RuntimeException {

    private HttpStatus httpStatus;
    private Object error;
    private String message;

    public ItemDetailsException() {
    }

    public ItemDetailsException(HttpStatus httpStatus, String msg) {
        this.message = msg;
        this.httpStatus = httpStatus;
    }

    public ItemDetailsException(HttpStatus httpStatus, Exception ex) {
        super(ex);
        this.httpStatus = httpStatus;
        this.error = ex;
    }

    public ItemDetailsException(String msg, Exception ex) {
        super(ex);
        this.message = msg;
    }
}

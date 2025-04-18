package cfp.wecare.transaction.exception;

import org.springframework.http.HttpStatus;

public class TransactionException extends RuntimeException{
    private HttpStatus httpStatus;
    private Object error;
    private String message;

    public TransactionException() {
    }

    public TransactionException(HttpStatus httpStatus, String msg) {
        this.message = msg;
        this.httpStatus = httpStatus;
    }

    public TransactionException(HttpStatus httpStatus, Exception ex) {
        super(ex);
        this.httpStatus = httpStatus;
        this.error = ex;
    }

    public TransactionException(String msg, Exception ex) {
        super(ex);
        this.message = msg;
    }
}

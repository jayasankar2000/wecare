package cfp.wecare.flow.ui.consume.item.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ItemException extends RuntimeException {

    private HttpStatus httpStatus;
    private String msg;

    public ItemException() {
    }

    public ItemException(HttpStatus httpStatus, String msg) {
        super(msg);
    }

    public ItemException(HttpStatus httpStatus, Throwable ex) {
        super(ex);
    }

    public ItemException(String msg, Throwable ex) {
        super(ex);
    }
}

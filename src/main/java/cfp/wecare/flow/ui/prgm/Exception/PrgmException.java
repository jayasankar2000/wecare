package cfp.wecare.flow.ui.prgm.Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class PrgmException extends RuntimeException{
    HttpStatus httpStatus;
    Object error;
    PrgmException(){

    }

    public PrgmException( HttpStatus httpStatus, String msg){
        super(msg);
        this.httpStatus = httpStatus;
    }

    public PrgmException(HttpStatus httpStatus, Exception ex){
        super(ex);
        this.httpStatus = httpStatus;
    }

    public PrgmException(String msg, Exception ex){
        super(ex);
        this.error = ex;
    }
}

package cfp.wecare.flow.ui.User.Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class UserException extends RuntimeException{
    HttpStatus httpStatus;
    Object error;
    UserException(){

    }

    public UserException( HttpStatus httpStatus, String msg){
        super(msg);
        this.httpStatus = httpStatus;
    }

    public UserException(HttpStatus httpStatus, Exception ex){
        super(ex);
        this.httpStatus = httpStatus;
    }

    public UserException(String msg, Exception ex){
        super(ex);
        this.error = ex;
    }
}

package cfp.wecare.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExceptionResponseObject {
    private int httpStatusCode;
    private HttpStatus httpStatus;
    private String message;
}

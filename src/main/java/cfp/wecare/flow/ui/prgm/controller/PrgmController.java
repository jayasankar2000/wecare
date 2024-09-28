package cfp.wecare.flow.ui.prgm.controller;

import cfp.wecare.dto.PrgmDto;
import cfp.wecare.flow.ui.prgm.Exception.PrgmException;
import cfp.wecare.flow.ui.prgm.service.PrgmService;
import cfp.wecare.util.ExceptionResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@RestController
public class PrgmController {

    @Autowired
    private PrgmService prgmService;

    @GetMapping(value = "/getPrgms")
    public List<PrgmDto> getAllPrgms() {
        return prgmService.getPrgms();
    }

    @GetMapping(value = "getPrgm")
    public PrgmDto getProgram(@PathVariable String prgmId) {
        try {
            return prgmService.getPrgm(prgmId);
        } catch (Exception ex) {
            if (ex instanceof PrgmException pe) {
                throw pe;
            }
            throw new PrgmException(HttpStatus.INTERNAL_SERVER_ERROR, ex);
        }
    }

    @ExceptionHandler(PrgmException.class)
    public ResponseEntity<ExceptionResponseObject> exceptionHandler(PrgmException ex, WebRequest request) {
        ExceptionResponseObject object = ExceptionResponseObject.builder().
                httpStatus(ex.getHttpStatus())
                .message(ex.getMessage())
                .httpStatusCode(ex.getHttpStatus().value())
                .build();
        return new ResponseEntity<>(object, ex.getHttpStatus());
    }
}

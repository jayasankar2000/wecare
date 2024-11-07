package cfp.wecare.flow.ui.prgm.controller;

import cfp.wecare.dto.PrgmDto;
import cfp.wecare.flow.ui.prgm.Exception.PrgmException;
import cfp.wecare.flow.ui.prgm.service.PrgmService;
import cfp.wecare.util.ExceptionResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PrgmController {

    @Autowired
    private PrgmService prgmService;

    @GetMapping(value = "/getPrgms")
    public ResponseEntity<List<PrgmDto>> getAllPrgms() {
        try {
            return ResponseEntity.ok(prgmService.getPrgms()) ;
        } catch (Exception ex) {
            if (ex instanceof PrgmException pe) {
                throw pe;
            }
            throw new PrgmException(HttpStatus.INTERNAL_SERVER_ERROR, ex);
        }
    }

    @GetMapping(value = "/getPrgm/{prgmId}")
    public ResponseEntity<PrgmDto> getProgram(@PathVariable String prgmId) {
        try {
            return ResponseEntity.ok(prgmService.getPrgm(prgmId));
        } catch (Exception ex) {
            if (ex instanceof PrgmException pe) {
                throw pe;
            }
            throw new PrgmException(HttpStatus.INTERNAL_SERVER_ERROR, ex);
        }
    }

    @PostMapping(value = "/super/create")
    public ResponseEntity<PrgmDto> createProgram(@RequestBody PrgmDto prgmDto) {
        try {
            return ResponseEntity.ok( prgmService.savePrgm(prgmDto));
        } catch (Exception e) {
            if (e instanceof PrgmException ex) {
                throw ex;
            } else {
                throw new PrgmException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }

    @PutMapping(value = "/super/update/{prgmId}")
    public ResponseEntity<PrgmDto> updateProgram(@PathVariable String prgmId,@RequestParam PrgmDto prgmDto) {
        try {
            return ResponseEntity.ok(prgmService.updateProgram(prgmId, prgmDto));
        } catch (Exception e) {
            if (e instanceof PrgmException ex) {
                throw ex;
            } else {
                throw new PrgmException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }

    @ExceptionHandler(value = PrgmException.class)
    public ResponseEntity<ExceptionResponseObject> exceptionHandler(PrgmException ex, WebRequest request) {
        ExceptionResponseObject object = ExceptionResponseObject.builder().
                httpStatus(ex.getHttpStatus())
                .message(ex.getMessage())
                .httpStatusCode(ex.getHttpStatus().value())
                .build();
        return new ResponseEntity<>(object, ex.getHttpStatus());
    }
}

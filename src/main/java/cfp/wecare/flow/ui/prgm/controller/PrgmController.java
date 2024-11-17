package cfp.wecare.flow.ui.prgm.controller;

import cfp.wecare.dto.PrgmDto;
import cfp.wecare.flow.ui.prgm.Exception.PrgmException;
import cfp.wecare.flow.ui.prgm.service.PrgmService;
import cfp.wecare.model.Role;
import cfp.wecare.util.ExceptionResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class PrgmController {

    @Autowired
    private PrgmService prgmService;

    @GetMapping(value = "/getPrgmsTest")
    @PreAuthorize("hasRole(T(cfp.wecare.model.Role).SIMPLE_USER.getRole())")
    public ResponseEntity<List<PrgmDto>> getAllPrgmsTest() {
        List<PrgmDto> lst = new ArrayList<>();
        PrgmDto prgmDto = PrgmDto.builder()
                .pgmId(UUID.randomUUID().toString())
                .pgmName("Pgm1")
                .stDate(LocalDate.now())
                .edDate(LocalDate.now().plusMonths(1))
                .build();
        lst.add(prgmDto);
        return ResponseEntity.ok(lst);
    }

    @GetMapping(value = "/getPrgms")
    public ResponseEntity<List<PrgmDto>> getAllPrgms() {
        try {
            return ResponseEntity.ok(prgmService.getPrgms());
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

    @PostMapping(value = "/super/createPrgm")
    public ResponseEntity<PrgmDto> createProgram(@RequestBody PrgmDto prgmDto) {
        try {
            return ResponseEntity.ok(prgmService.savePrgm(prgmDto));
        } catch (Exception e) {
            if (e instanceof PrgmException ex) {
                throw ex;
            } else {
                throw new PrgmException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }

    @PutMapping(value = "/super/updatePrgm/{prgmId}")
    public ResponseEntity<PrgmDto> updateProgram(@PathVariable String prgmId, @RequestParam PrgmDto prgmDto) {
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

    @DeleteMapping(value = "/super/deletePrgm/{prgmId}")
    public ResponseEntity<String> deleteProgram(@PathVariable String prgmId) {
        try {
            prgmService.deleteProgram(prgmId);
        } catch (Exception e) {
            if (e instanceof PrgmException ex) {
                throw ex;
            }
            throw new PrgmException(HttpStatus.INTERNAL_SERVER_ERROR, "could not delete program!! please try again" + e.getMessage());
        }
        return ResponseEntity.ok("Program deleted");
    }

    @ExceptionHandler(value = PrgmException.class)
    public ResponseEntity<ExceptionResponseObject> exceptionHandler(PrgmException ex) {
        ExceptionResponseObject object = ExceptionResponseObject.builder().
                httpStatus(ex.getHttpStatus())
                .message(ex.getMessage())
                .httpStatusCode(ex.getHttpStatus().value())
                .build();
        return new ResponseEntity<>(object, ex.getHttpStatus());
    }
}

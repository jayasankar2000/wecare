package cfp.wecare.flow.ui.publicAccess.controller;

import cfp.wecare.dto.PrgmDto;
import cfp.wecare.dto.UserDto;
import cfp.wecare.flow.ui.User.Exception.UserException;
import cfp.wecare.flow.ui.prgm.Exception.PrgmException;
import cfp.wecare.flow.ui.prgm.service.PrgmService;
import cfp.wecare.flow.ui.publicAccess.Dto.UserLoginDto;
import cfp.wecare.flow.ui.publicAccess.Exception.PublicAccessException;
import cfp.wecare.service.UserService;
import cfp.wecare.util.ExceptionResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequestMapping(value = "/public")
@RestController
public class PublicController {
    @Autowired
    private PrgmService prgmService;
    @Autowired
    private UserService userService;

    @GetMapping(value = "/getPrgmsTest")
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

    @PostMapping(value = "/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDto userDto) {
        try {
            if(userDto == null || !StringUtils.hasText(userDto.getUserName()) || !StringUtils.hasText(userDto.getPassword())){
                throw new UserException(HttpStatus.BAD_REQUEST, "User name and password is mandatory");
            }
            if(!userService.isUserNotExists(userDto)){
                throw new UserException(HttpStatus.CONFLICT, "The username already exists!! please try with a different user name");
            }
             userService.registerUser(userDto);
            return ResponseEntity.ok("User Registered Successfully");
        } catch (UserException ex) {
            throw new PublicAccessException(ex.getHttpStatus(), ex.getMessage());
        } catch (Exception e) {
            throw new PublicAccessException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while registering!! Please try again");
        }
    }

    @GetMapping(value = "/getPrgms")
    public ResponseEntity<List<PrgmDto>> getAllPrgms() {
        try {
            return ResponseEntity.ok(prgmService.getPrgms());
        } catch (PrgmException e) {
            throw new PublicAccessException(e.getHttpStatus(), e.getMessage());
        } catch (Exception ex) {
            throw new PublicAccessException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while fetching programs!! please try after some time");
        }
    }

    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDto userLoginDto) {
        if (userLoginDto == null || !StringUtils.hasText(userLoginDto.getUserName()) || !StringUtils.hasText(userLoginDto.getPassword())) {
            throw new UserException(HttpStatus.BAD_REQUEST, "Username and password are mandatory!!");
        }
        if (userService.login(userLoginDto)) {
            return ResponseEntity.ok("login Successful");
        }
        return ResponseEntity.ok("Invalid username or password.");
    }

    @ExceptionHandler(value = PublicAccessException.class)
    public ResponseEntity<ExceptionResponseObject> exceptionHandler(PublicAccessException accessException) {
        ExceptionResponseObject object = ExceptionResponseObject
                .builder()
                .httpStatus(accessException.getHttpStatus())
                .message(accessException.getMessage())
                .httpStatusCode(accessException.getHttpStatus().value())
                .build();
        return new ResponseEntity<>(object, accessException.getHttpStatus());
    }
}

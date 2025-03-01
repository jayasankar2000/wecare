package cfp.wecare.flow.ui.publicAccess.controller;

import cfp.wecare.dto.PrgmDto;
import cfp.wecare.dto.UserInputDto;
import cfp.wecare.flow.ui.User.Exception.UserException;
import cfp.wecare.flow.ui.prgm.Exception.PrgmException;
import cfp.wecare.flow.ui.prgm.service.PrgmService;
import cfp.wecare.flow.ui.publicAccess.Dto.LoginResponseDto;
import cfp.wecare.flow.ui.publicAccess.Dto.UserLoginDto;
import cfp.wecare.flow.ui.publicAccess.Exception.PublicAccessException;
import cfp.wecare.service.UserService;
import cfp.wecare.util.ExceptionResponseObject;
import cfp.wecare.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping(value = "/getPrgmsTest")
    public ResponseEntity<List<PrgmDto>> getAllPrgmsTest() {
        List<PrgmDto> lst = new ArrayList<>();
        PrgmDto prgmDto = PrgmDto.builder().pgmId(UUID.randomUUID().toString()).pgmName("Pgm1").stDate(LocalDate.now()).edDate(LocalDate.now().plusMonths(1)).build();
        lst.add(prgmDto);
        return ResponseEntity.ok(lst);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<String> registerUser(@RequestBody UserInputDto userInputDto) {
        try {
            if (userInputDto == null || !StringUtils.hasText(userInputDto.getUserName()) || !StringUtils.hasText(userInputDto.getPassword())) {
                throw new UserException(HttpStatus.BAD_REQUEST, "User name and password is mandatory");
            }
            if (!userService.isUserNotExists(userInputDto)) {
                throw new UserException(HttpStatus.CONFLICT, "The username already exists!! please try with a different user name");
            }
            userService.registerUser(userInputDto);
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
    public ResponseEntity<LoginResponseDto> login(@RequestBody UserLoginDto userLoginDto) {
        try {
            if (userLoginDto == null || !StringUtils.hasText(userLoginDto.getUserName()) || !StringUtils.hasText(userLoginDto.getPassword())) {
                throw new UserException(HttpStatus.BAD_REQUEST, "Username and password are mandatory!!");
            }
            return ResponseEntity.ok(userService.login(userLoginDto));
        } catch (UserException ue) {
            throw new PublicAccessException(HttpStatus.BAD_REQUEST, ue.getMessage());
        } catch (PublicAccessException pe) {
            throw pe;
        } catch (Exception e) {
            throw new PublicAccessException(HttpStatus.INTERNAL_SERVER_ERROR, "login failed with internal server error");
        }
    }

    @GetMapping(value = "/refresh")
    public ResponseEntity<String> refreshToken(HttpServletRequest request) {
        try {
            String newAccessToken = null;
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
                newAccessToken = userService.generateNewAccessToken(authHeader);
                if (StringUtils.hasText(newAccessToken)) {
                    return ResponseEntity.ok(newAccessToken);
                }
            }
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new PublicAccessException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping(value = "/register-admin")
    public ResponseEntity<String> registerAdmin(@RequestBody UserInputDto userInputDto) {
        try {
            if (userInputDto == null || !StringUtils.hasText(userInputDto.getUserName()) || !StringUtils.hasText(userInputDto.getPassword())) {
                throw new UserException(HttpStatus.BAD_REQUEST, "User name and password is mandatory");
            }
            if (!userService.isUserNotExists(userInputDto)) {
                throw new UserException(HttpStatus.CONFLICT, "The username already exists!! please try with a different user name");
            }
            userService.registerAdmin(userInputDto);
            return ResponseEntity.ok("User Registered Successfully");
        } catch (UserException ex) {
            throw new PublicAccessException(ex.getHttpStatus(), ex.getMessage());
        } catch (Exception e) {
            throw new PublicAccessException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while registering!! Please try again");
        }
    }

    @ExceptionHandler(value = PublicAccessException.class)
    public ResponseEntity<ExceptionResponseObject> exceptionHandler(PublicAccessException accessException) {
        ExceptionResponseObject object = ExceptionResponseObject.builder().httpStatus(accessException.getHttpStatus()).message(accessException.getMessage()).httpStatusCode(accessException.getHttpStatus().value()).build();
        return new ResponseEntity<>(object, accessException.getHttpStatus());
    }
}

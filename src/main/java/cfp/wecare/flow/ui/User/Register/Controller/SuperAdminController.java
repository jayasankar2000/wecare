package cfp.wecare.flow.ui.User.Register.Controller;

import cfp.wecare.dto.UserDetailsDto;
import cfp.wecare.dto.UserInputDto;
import cfp.wecare.flow.ui.User.Exception.UserException;
import cfp.wecare.service.UserService;
import cfp.wecare.util.ExceptionResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value = "/super")
@RestController
public class SuperAdminController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/users")
    public ResponseEntity<List<UserDetailsDto>> getUsers() {
        try {
            List<UserDetailsDto> allUsers = userService.getAllUsers();
            return ResponseEntity.ok(allUsers);
        } catch (UserException ex) {
            throw ex;
        } catch (Exception e) {
            throw new UserException(HttpStatus.INTERNAL_SERVER_ERROR, "Fetching all users failed with internal server error");
        }
    }

    @PutMapping(value = "/edit-role/{userId}")
    public ResponseEntity<UserInputDto> editUserRole(@PathVariable String userId, @RequestParam String role) {
        try {
            return ResponseEntity.ok(userService.editUserRole(userId, role));
        } catch (UserException ex) {
            throw ex;
        } catch (Exception e) {
            throw new UserException(HttpStatus.INTERNAL_SERVER_ERROR, "editing the role failed with internal server error");
        }
    }

    @ExceptionHandler(value = UserException.class)
    public ResponseEntity<ExceptionResponseObject> exceptionHandler(UserException ex) {
        ExceptionResponseObject object = ExceptionResponseObject.builder()
                .httpStatusCode(ex.getHttpStatus().value())
                .httpStatus(ex.getHttpStatus())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(object, ex.getHttpStatus());
    }
}

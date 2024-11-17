package cfp.wecare.flow.ui.User.Register.Controller;

import cfp.wecare.dto.UserDto;
import cfp.wecare.flow.ui.User.Exception.UserException;
import cfp.wecare.service.UserService;
import cfp.wecare.util.ExceptionResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value = "/api")
public class RegisterController {

    @Autowired
    UserService userService;

    @PostMapping(value = "user/register")
    public ResponseEntity<String> registerUser(@RequestParam UserDto userDto) {
        try {
            userService.registerUser(userDto);
            return ResponseEntity.ok("User Registered Successfully");
        } catch (UserException ex) {
            throw ex;
        } catch (Exception e) {
            throw new UserException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while registering!! Please try again");
        }
    }

    @GetMapping(value = "/admin/getUsers")
    public ResponseEntity<List<UserDto>> getUsers() {
        try {
            List<UserDto> allUsers = userService.getAllUsers();
            return ResponseEntity.ok(allUsers);
        } catch (UserException ex) {
            throw ex;
        } catch (Exception e) {
            throw new UserException(HttpStatus.INTERNAL_SERVER_ERROR, "Fetching all users failed with internal server error");
        }
    }

    @PutMapping(value = "/admin/users/editRole/{userId}")
    public ResponseEntity<UserDto> editUserRole(@PathVariable String userId, @RequestParam String role) {
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

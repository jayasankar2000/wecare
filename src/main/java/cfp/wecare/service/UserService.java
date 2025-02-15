package cfp.wecare.service;

import cfp.wecare.Repository.UserRepository;
import cfp.wecare.dto.UserDto;
import cfp.wecare.flow.ui.User.Exception.UserException;
import cfp.wecare.flow.ui.publicAccess.Dto.UserLoginDto;
import cfp.wecare.model.Role;
import cfp.wecare.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public void registerUser(UserDto userDto) {
        User user = User.builder()
                .userId(UUID.randomUUID().toString())
                .userName(userDto.getUserName())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .role(Role.SIMPLE_USER.getRole())
                .build();
        userRepository.save(user);
    }

    public List<UserDto> getAllUsers() {
        List<User> usersDao = userRepository.findAll();
        return usersDao.stream()
                .map(user -> {
                    return UserDto.builder()
                            .userId(user.getUserId())
                            .userName(user.getUserName())
                            .role(user.getRole())
                            .build();
                })
                .toList();
    }

    public UserDto editUserRole(String userId, String role) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserException(HttpStatus.BAD_REQUEST, "User is not present"));
            boolean match = Arrays.stream(Role.values())
                    .map(Role::getRole)
                    .toList()
                    .stream()
                    .anyMatch(role1 -> (role1.equals(role)));
            if (!match) {
                throw new UserException(HttpStatus.BAD_REQUEST, "The provided role is not applicable");
            }
            user.setRole(role);
            User save = userRepository.save(user);
            return UserDto.builder()
                    .userName(save.getUserName())
                    .role(save.getRole())
                    .userId(save.getUserId())
                    .build();
        } catch (UserException e) {
            throw e;
        } catch (Exception e) {
            throw new UserException(HttpStatus.INTERNAL_SERVER_ERROR, "changing user role failed with internal server error");
        }
    }

    public boolean login(UserLoginDto userLoginDto) {
        String username = userLoginDto.getUserName();
        Optional<User> user = userRepository.findByUserName(username);
        User dbUser = user.orElse(null);
        return dbUser != null && passwordEncoder.matches(userLoginDto.getPassword(), dbUser.getPassword());
    }

    public boolean isUserNotExists(UserDto userDto) {
        String userName = userDto.getUserName();
        Optional<User> user = userRepository.findByUserName(userName);
        return user.isEmpty();
    }
}

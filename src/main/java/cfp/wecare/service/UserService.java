package cfp.wecare.service;

import cfp.wecare.Repository.UserRepository;
import cfp.wecare.dto.UserDetailsDto;
import cfp.wecare.dto.UserInputDto;
import cfp.wecare.flow.ui.User.Exception.UserException;
import cfp.wecare.flow.ui.publicAccess.Dto.LoginResponseDto;
import cfp.wecare.flow.ui.publicAccess.Dto.UserLoginDto;
import cfp.wecare.flow.ui.publicAccess.Exception.PublicAccessException;
import cfp.wecare.model.Role;
import cfp.wecare.model.User;
import cfp.wecare.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    @Autowired
    private MyUserDetailsService myUserDetailsService;
    @Autowired
    private JwtUtil jwtUtil;

    public void registerUser(UserInputDto userInputDto) {
        User user = User.builder()
                .userId(UUID.randomUUID().toString())
                .userName(userInputDto.getUserName())
                .password(passwordEncoder.encode(userInputDto.getPassword()))
                .role(Role.SIMPLE_USER.getRole())
                .build();
        userRepository.save(user);
    }

    public List<UserDetailsDto> getAllUsers() {
        List<User> usersDaos = userRepository.findAll();
        return usersDaos.stream()
                .map(user -> {
                    return UserDetailsDto.builder()
                            .userId(user.getUserId())
                            .userName(user.getUserName())
                            .role(user.getRole())
                            .build();
                })
                .toList();
    }

    public UserInputDto editUserRole(String userId, String role) {
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
            return UserInputDto.builder()
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

    public LoginResponseDto login(UserLoginDto userLoginDto) {
        try {
            String username = userLoginDto.getUserName();
            UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
            String refreshToken = jwtUtil.generateRefreshToken(userDetails);
            Optional<User> dbUser = userRepository.findByUserName(username);
            if (dbUser.isPresent()) {
                User user = dbUser.get();
                user.setRefreshToken(jwtUtil.generateRefreshToken(userDetails));
                userRepository.save(user);
            }
            return LoginResponseDto.builder()
                    .login(username)
                    .accessToken(jwtUtil.generateAccessToken(userDetails))
                    .build();
        } catch (UsernameNotFoundException exception) {
            throw new PublicAccessException(HttpStatus.NOT_FOUND, exception.getMessage());
        } catch (Exception e) {
            throw new PublicAccessException(HttpStatus.INTERNAL_SERVER_ERROR, "Login failed!! Please try again");
        }
    }

    public boolean isUserNotExists(UserInputDto userDto) {
        String userName = userDto.getUserName();
        Optional<User> user = userRepository.findByUserName(userName);
        return user.isEmpty();
    }

    public String generateNewAccessToken(String authHeader) {
        String jwt = authHeader.substring(7);
        String userName = jwtUtil.extractUserName(jwt);
        Optional<User> user = userRepository.findByUserName(userName);
        String newAccessToken = null;
        if (user.isPresent()) {
            String refreshToken = user.get().getRefreshToken();
            if (jwtUtil.isTokenValid(refreshToken)) {
                UserDetails userDetails = myUserDetailsService.loadUserByUsername(userName);
                newAccessToken = jwtUtil.generateAccessToken(userDetails);
            }
        }
        return newAccessToken;
    }

    public void registerAdmin(UserInputDto userInputDto) {
        User user = User.builder()
                .userId(UUID.randomUUID().toString())
                .userName(userInputDto.getUserName())
                .password(passwordEncoder.encode(userInputDto.getPassword()))
                .role(Role.SUPER_ADMIN.getRole())
                .build();
        userRepository.save(user);
    }
}

package atdd.user.application;

import atdd.security.FailedLoginException;
import atdd.security.JwtTokenProvider;
import atdd.user.application.dto.LoginRequestView;
import atdd.user.application.dto.LoginResponseView;
import atdd.user.application.dto.UserRequestView;
import atdd.user.application.dto.UserResponseView;
import atdd.user.domain.User;
import atdd.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public UserResponseView createUser(UserRequestView userRequestView) {
        return UserResponseView.of(userRepository.save(userRequestView.toUser()));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public UserResponseView retrieveUser(User user) {
        User persistUser = userRepository.findUserByEmail(user.getEmail());
        return UserResponseView.of(persistUser);
    }

    public LoginResponseView login(LoginRequestView loginRequestView) {
        User userInfo = userRepository.findUserByEmail(loginRequestView.getEmail());

        if (Objects.isNull(userInfo) || !userInfo.validatePassword(loginRequestView.getPassword())) {
            throw new FailedLoginException("Invalid user info");
        }

        String token = jwtTokenProvider.createToken(loginRequestView.getEmail());
        return LoginResponseView.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .build();
    }
}

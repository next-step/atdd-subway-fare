package atdd.user.web;

import atdd.user.application.UserService;
import atdd.user.application.dto.CreateUserRequestView;
import atdd.user.application.dto.LoginRequestView;
import atdd.user.application.dto.UserResponseView;
import atdd.user.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserController {
    final String ACCESS_TOKEN_HEADER = "Authorization";

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseView> createUser(@RequestBody CreateUserRequestView view) {
        final User savedUser = userService.save(view.toUser());
        return ResponseEntity.created(URI.create("/users/" + savedUser.getId()))
                .body(new UserResponseView(savedUser));
    }

    @GetMapping("/me")
    public ResponseEntity retrieveUser(@LoginUser User user) {
        User persistUser = userService.findUserByEmail(user.getEmail());
        return ResponseEntity.ok().body(new UserResponseView(persistUser));
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody final LoginRequestView view) {
        String token = userService.login(view.getEmail(), view.getPassword());

        return ResponseEntity.noContent()
                .header(ACCESS_TOKEN_HEADER, token).build();
    }
}

package atdd.user.web;

import atdd.user.application.UserService;
import atdd.user.application.dto.LoginRequestView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("login")
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity login(@RequestBody LoginRequestView loginRequestView) {
        return ResponseEntity.ok(userService.login(loginRequestView));
    }
}

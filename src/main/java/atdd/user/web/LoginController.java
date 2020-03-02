package atdd.user.web;

import atdd.user.application.UserService;
import atdd.user.application.dto.LoginRequestView;
import atdd.user.application.dto.LoginResponseResource;
import atdd.user.application.dto.LoginResponseView;
import atdd.user.domain.User;
import atdd.user.jwt.JwtTokenProvider;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static atdd.Constant.LOGIN_BASE_URI;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(LOGIN_BASE_URI)
public class LoginController {
    private UserService userService;
    private JwtTokenProvider jwtTokenProvider;

    public LoginController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping
    public ResponseEntity login(@RequestBody LoginRequestView request) {
        User user = userService.findByEmail(request.getEmail());
        boolean isMatch = user.getPassword().equals(request.getPassword());
        if (!isMatch) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .build();
        }
        LoginResponseView response = new LoginResponseView(request.getAccessToken(), request.getTokenType());
        LoginResponseResource resource = new LoginResponseResource(response);
        resource.add(linkTo(LoginController.class)
                .withSelfRel());
        resource.add(new Link("/docs/api-guide.html#resources-login")
                .withRel("profile"));
        return ResponseEntity
                .created(URI.create("/oauth/token"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(resource);
    }
}

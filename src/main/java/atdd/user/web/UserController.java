package atdd.user.web;

import atdd.favorite.application.dto.LoginUser;
import atdd.user.application.UserService;
import atdd.user.application.dto.CreateUserRequestView;
import atdd.user.application.dto.UserResponseResource;
import atdd.user.application.dto.UserResponseView;
import atdd.user.domain.User;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

import static atdd.Constant.USER_BASE_URI;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(USER_BASE_URI)
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid CreateUserRequestView request,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        User user = isExistingUser(request);
        if (user != null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .build();
        }

        UserResponseView response = userService.createUser(request);
        UserResponseResource resource = new UserResponseResource(response);
        resource.add(linkTo(UserController.class).withSelfRel());
        resource.add(new Link("/docs/api-guide.html#resources-users-create")
                .withRel("profile"));
        return ResponseEntity
                .created(URI.create("/" + response.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        userService.deleteUser(id);
        UserResponseResource resource = new UserResponseResource(new UserResponseView());
        resource.add(linkTo(UserController.class)
                .withSelfRel());
        resource.add(new Link("/docs/api-guide.html#resources-users-delete")
                .withRel("profile"));
        return ResponseEntity
                .ok(resource);
    }

    @GetMapping("/me")
    public ResponseEntity retrieveInfo(@LoginUser User user) {
        UserResponseView response
                = new UserResponseView(user.getEmail(), user.getName(), user.getPassword());
        UserResponseResource resource
                = new UserResponseResource(response);
        resource.add(linkTo(UserController.class).slash("me")
                .withSelfRel());
        resource.add(new Link("/docs/api-guide.html#resources-users-me")
                .withRel("profile"));
        return ResponseEntity
                .ok(resource);
    }

    private User isExistingUser(CreateUserRequestView request) {
        User user = userService.findByEmail(request.getEmail());
        return user;
    }
}

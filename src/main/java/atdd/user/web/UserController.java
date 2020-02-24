package atdd.user.web;

import atdd.user.application.UserService;
import atdd.user.application.dto.CreateUserRequestView;
import atdd.user.application.dto.UserResource;
import atdd.user.application.dto.UserResponseView;
import atdd.user.domain.User;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public ResponseEntity create(@RequestBody @Valid CreateUserRequestView request, BindingResult bindingResult) {
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
        UserResource userResource = new UserResource(response);
        userResource.add(linkTo(UserController.class).slash(response.getId()).withSelfRel());
        userResource.add(linkTo(UserController.class).slash(response.getId()).withRel("users-delete"));
        userResource.add(linkTo(UserController.class).slash("me").withRel("users-me"));
        userResource.add(new Link("/docs/api-guide.html#resources-users-create").withRel("profile"));
        return ResponseEntity
                .created(URI.create("/" + response.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(userResource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResource> delete(@PathVariable Long id) {
        Long userId = userService.deleteUser(id);
        UserResponseView response = new UserResponseView();
        response.insertId(id);
        UserResource userResource = new UserResource(response);
        userResource.add(linkTo(UserController.class).slash(response.getId()).withSelfRel());
        userResource.add(linkTo(UserController.class).withRel("users-create"));
        userResource.add(new Link("/docs/api-guide.html#resources-users-delete").withRel("profile"));
        return ResponseEntity
                .ok(userResource);


    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseView> retrieveInfo(HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        User user = userService.findByEmail(email);
        UserResponseView response = new UserResponseView(user.getEmail(), user.getName(), user.getPassword());
        return ResponseEntity
                .ok()
                .body(response);
    }

    private User isExistingUser(CreateUserRequestView request) {
        User user = userService.findByEmail(request.getEmail());
        return user;
    }
}

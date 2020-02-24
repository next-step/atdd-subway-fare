package atdd.user.application.dto;

import atdd.user.web.UserController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class UserResource extends EntityModel<UserResponseView> {
    public UserResource(UserResponseView userResponseView, Link... links) {
        super(userResponseView, links);
        add(linkTo(UserController.class).slash(userResponseView.getId()).withSelfRel());
    }
}

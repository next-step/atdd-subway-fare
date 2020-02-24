package atdd.user.application.dto;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

public class UserResource extends EntityModel<UserResponseView> {
    public UserResource(UserResponseView userResponseView, Link... links) {
        super(userResponseView, links);
    }
}

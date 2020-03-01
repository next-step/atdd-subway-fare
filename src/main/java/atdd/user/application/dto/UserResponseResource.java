package atdd.user.application.dto;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

public class UserResponseResource extends EntityModel<UserResponseView> {
    public UserResponseResource(UserResponseView content, Link... links) {
        super(content, links);
    }
}
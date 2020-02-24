package atdd.user.application.dto;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

public class LoginResource extends EntityModel<LoginResponseView> {
    public LoginResource(LoginResponseView content, Link... links) {
        super(content, links);
    }
}

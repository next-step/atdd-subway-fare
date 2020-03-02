package atdd.path.application.dto;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

public class PathResponseResource extends EntityModel<PathResponseView> {
    public PathResponseResource(PathResponseView content, Link... links) {
        super(content, links);
    }
}

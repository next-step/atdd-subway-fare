package atdd.path.application.dto;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

public class PathResource extends EntityModel<PathResponseView> {
    public PathResource(PathResponseView content, Link... links) {
        super(content, links);
    }
}

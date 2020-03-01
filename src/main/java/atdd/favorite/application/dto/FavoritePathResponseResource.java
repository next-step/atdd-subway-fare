package atdd.favorite.application.dto;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

public class FavoritePathResponseResource extends EntityModel<FavoritePathResponseView> {
    public FavoritePathResponseResource(FavoritePathResponseView content, Link... links) {
        super(content, links);
    }
}

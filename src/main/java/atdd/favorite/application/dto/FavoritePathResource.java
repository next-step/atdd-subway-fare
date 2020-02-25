package atdd.favorite.application.dto;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

public class FavoritePathResource extends EntityModel<FavoritePathResponseView> {
    public FavoritePathResource(FavoritePathResponseView content, Link... links) {
        super(content, links);
    }
}

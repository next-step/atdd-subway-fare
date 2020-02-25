package atdd.favorite.application.dto;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

public class FavoritePathListResource  extends EntityModel<FavoritePathListResponseView> {
    public FavoritePathListResource(FavoritePathListResponseView content, Link... links) {
        super(content, links);
    }
}

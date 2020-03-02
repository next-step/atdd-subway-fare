package atdd.favorite.application.dto;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

public class FavoritePathListResponseResource extends EntityModel<FavoritePathListResponseView> {
    public FavoritePathListResponseResource(FavoritePathListResponseView content, Link... links) {
        super(content, links);
    }
}

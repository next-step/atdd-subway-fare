package atdd.favorite.application.dto;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

public class FavoriteStationResponseResource extends EntityModel<FavoriteStationResponseView> {
    public FavoriteStationResponseResource(FavoriteStationResponseView content, Link... links) {
        super(content, links);
    }
}

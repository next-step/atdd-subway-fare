package atdd.favorite.application.dto;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

public class FavoriteStationResource extends EntityModel<FavoriteStationResponseView> {
    public FavoriteStationResource(FavoriteStationResponseView content, Link... links) {
        super(content, links);
    }
}

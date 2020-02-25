package atdd.favorite.application.dto;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

public class FavoriteStationListResource extends EntityModel<FavoriteStationsListResponseView> {
    public FavoriteStationListResource(FavoriteStationsListResponseView content, Link... links) {
        super(content, links);
    }
}

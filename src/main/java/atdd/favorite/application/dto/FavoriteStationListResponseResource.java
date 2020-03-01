package atdd.favorite.application.dto;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

public class FavoriteStationListResponseResource extends EntityModel<FavoriteStationListResponseVIew> {
    public FavoriteStationListResponseResource(FavoriteStationListResponseVIew content, Link... links) {
        super(content, links);
    }
}

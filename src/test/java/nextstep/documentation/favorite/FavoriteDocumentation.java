package nextstep.documentation.favorite;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static nextstep.api.favorite.acceptance.FavoriteSteps.모든_즐겨찾기_조회를_요청한다;
import static nextstep.api.favorite.acceptance.FavoriteSteps.즐겨찾기_생성을_요청한다;
import static nextstep.api.favorite.acceptance.FavoriteSteps.즐겨찾기_제거를_요청한다;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationExtension;

import nextstep.api.favorite.application.FavoriteService;
import nextstep.api.favorite.application.dto.FavoriteResponse;
import nextstep.api.subway.applicaion.station.dto.StationResponse;
import nextstep.documentation.Documentation;

@ExtendWith(RestDocumentationExtension.class)
class FavoriteDocumentation extends Documentation {
    @MockBean
    private FavoriteService favoriteService;

    private final String name = "강남역";
    private final FavoriteResponse response = new FavoriteResponse(
            1L,
            new StationResponse(1L, "강남역"),
            new StationResponse(2L, "삼성역")
    );

    @Test
    void saveFavorite() {
        when(favoriteService.saveFavorite(any(), any())).thenReturn(response);

        즐겨찾기_생성을_요청한다("token", 1L, 2L, makeRequestSpec("favorite-create"));
    }

    @Test
    void deleteFavorites() {
        doNothing().when(favoriteService).deleteFavorite(any(), anyLong());

        즐겨찾기_제거를_요청한다("token", 1L, makeRequestSpec("favorite-delete"));
    }

    @Test
    void showFavorites() {
        when(favoriteService.findAllFavorites(any())).thenReturn(List.of(response));

        모든_즐겨찾기_조회를_요청한다("token", makeRequestSpec("favorite-show"));
    }
}

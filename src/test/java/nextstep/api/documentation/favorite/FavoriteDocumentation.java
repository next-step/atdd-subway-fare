package nextstep.api.documentation.favorite;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import nextstep.api.acceptance.favorite.FavoriteSteps;
import nextstep.api.documentation.Documentation;
import nextstep.api.favorite.application.FavoriteService;
import nextstep.api.favorite.application.dto.FavoriteResponse;
import nextstep.api.subway.applicaion.station.dto.StationResponse;

class FavoriteDocumentation extends Documentation {
    @MockBean
    private FavoriteService favoriteService;

    private final FavoriteResponse response = new FavoriteResponse(1L,
            new StationResponse(1L, "강남역"),
            new StationResponse(2L, "삼성역")
    );

    @Test
    void saveFavorite() {
        when(favoriteService.saveFavorite(any(), any())).thenReturn(response);

        FavoriteSteps.즐겨찾기_생성_요청("token", 1L, 2L, makeRequestSpec("favorite-create"));
    }

    @Test
    void deleteFavorites() {
        doNothing().when(favoriteService).deleteFavorite(any(), anyLong());

        FavoriteSteps.즐겨찾기_제거_요청("token", 1L, makeRequestSpec("favorite-delete"));
    }

    @Test
    void showFavorites() {
        when(favoriteService.findAllFavorites(any())).thenReturn(List.of(response));

        FavoriteSteps.즐겨찾기_전체조회_요청("token", makeRequestSpec("favorite-show"));
    }
}

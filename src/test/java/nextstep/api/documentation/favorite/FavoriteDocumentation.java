package nextstep.api.documentation.favorite;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import static nextstep.api.acceptance.favorite.FavoriteSteps.즐겨찾기_생성_요청;
import static nextstep.api.acceptance.favorite.FavoriteSteps.즐겨찾기_전체조회_요청;
import static nextstep.api.acceptance.favorite.FavoriteSteps.즐겨찾기_제거_요청;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import nextstep.api.auth.support.JwtTokenProvider;
import nextstep.api.documentation.Documentation;
import nextstep.api.favorite.application.FavoriteService;
import nextstep.api.favorite.application.dto.FavoriteResponse;
import nextstep.api.subway.applicaion.station.dto.StationResponse;

class FavoriteDocumentation extends Documentation {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private FavoriteService favoriteService;

    private final FavoriteResponse response = new FavoriteResponse(1L,
            new StationResponse(1L, "강남역"),
            new StationResponse(2L, "삼성역")
    );

    private String token;

    @BeforeEach
    void setUp() {
        token = jwtTokenProvider.createToken(token, "role");
    }

    @Test
    void saveFavorite() {
        when(favoriteService.saveFavorite(any(), any())).thenReturn(response);

        즐겨찾기_생성_요청(token, 1L, 2L, makeRequestSpec(
                document("favorite-create",
                        requestHeaders(
                                headerWithName("Authorization").description("bearer 토큰")
                        )
                )
        ));
    }

    @Test
    void deleteFavorites() {
        doNothing().when(favoriteService).deleteFavorite(any(), anyLong());

        즐겨찾기_제거_요청(token, 1L, makeRequestSpec(
                document("favorite-delete",
                        pathParameters(
                                parameterWithName("id").description("즐겨찾기 id")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("bearer 토큰")
                        ),
                        pathParameters(
                                parameterWithName("id").description("즐겨찾기 id")
                        )
                )
        ));
    }

    @Test
    void showFavorites() {
        when(favoriteService.findAllFavorites(any())).thenReturn(List.of(response));

        즐겨찾기_전체조회_요청(token, makeRequestSpec(
                document("favorite-show",
                        requestHeaders(
                                headerWithName("Authorization").description("bearer 토큰")
                        ),
                        responseFields(
                                fieldWithPath("[].id").description("즐겨찾기 id"),
                                fieldWithPath("[].source.id").description("출발역 id"),
                                fieldWithPath("[].source.name").description("출발역 이름"),
                                fieldWithPath("[].target.id").description("도착역 id"),
                                fieldWithPath("[].target.name").description("도착역 이름")
                        )
                )
        ));
    }
}

package nextstep.subway.path.documentation;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.Documentation;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.acceptance.PathSteps;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.RestDocumentationContextProvider;

import java.util.Arrays;

import static nextstep.subway.line.acceptance.LineSteps.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberSteps.로그인_되어_있음;
import static nextstep.subway.member.MemberSteps.회원_생성_요청;
import static nextstep.subway.path.acceptance.PathSteps.*;
import static nextstep.subway.station.StationSteps.지하철역_등록되어_있음;

public class PathDocumentation extends Documentation {

    private StationResponse 교대역;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 남부터미널역;
    private LineResponse 삼호선;

    public static final String ADULT_EMAIL = "adult@email.com";
    public static final String PASSWORD = "password";
    public static final int ADULT_AGE = 22;
    private TokenResponse adultTokenResponse;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        super.setUp(restDocumentation);
        회원_생성_요청(ADULT_EMAIL, PASSWORD, ADULT_AGE);

        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        LineResponse 이호선 = 지하철_노선_등록되어_있음("2호선", "green", 교대역, 강남역, 70, 70);
        LineResponse 신분당선 = 지하철_노선_등록되어_있음("신분당선", "green", 강남역, 양재역, 30, 30);
        삼호선 = 지하철_노선_등록되어_있음("3호선", "green", 교대역, 남부터미널역, 16, 16);

        지하철_노선에_지하철역_등록_요청(삼호선, 남부터미널역, 양재역, 22, 20);
    }


    @Test
    void path() {
        //given
        adultTokenResponse = 로그인_되어_있음(ADULT_EMAIL, PASSWORD);

        //when
        ExtractableResponse<Response> response = 두_역의_최단거리_탐색_요청(this,adultTokenResponse, 양재역.getId(), 교대역.getId());

        //then
        경로_응답됨(response, Arrays.asList(양재역.getId(), 남부터미널역.getId(), 교대역.getId()), 38, 36);
        PathSteps.경로_요금_일치함(response, 1850);
    }
}


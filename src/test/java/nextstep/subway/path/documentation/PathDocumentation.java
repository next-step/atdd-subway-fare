package nextstep.subway.path.documentation;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.Documentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;

import static nextstep.subway.line.acceptance.LineSteps.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSteps.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.station.StationSteps.지하철역_등록되어_있음;

public class PathDocumentation extends Documentation {
    private StationResponse 교대역;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 남부터미널역;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        super.setUp(restDocumentation);

        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        지하철_노선_등록되어_있음(new LineRequest("2호선", "bg-green-600", 교대역.getId(), 강남역.getId(), 10, 10));
        지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10, 10));
        LineResponse 삼호선 = 지하철_노선_등록되어_있음(new LineRequest("3호선", "bg-orange-600", 교대역.getId(), 남부터미널역.getId(), 2, 10)).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 남부터미널역, 양재역, 3, 10);
    }

    @Test
    void path() {
        given("path")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 교대역.getId())
                .queryParam("target", 양재역.getId())
                .queryParam("type", "DISTANCE")
                .when().get("/paths")
                .then().log().all().extract();
    }

    private RequestSpecification given(String identifier) {
        // TODO: 문서화를 위한 로직을 완성시키세요.
        return RestAssured
                .given().log().all();
    }
}


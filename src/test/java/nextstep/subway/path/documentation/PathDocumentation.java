package nextstep.subway.path.documentation;

import io.restassured.RestAssured;
import nextstep.subway.Documentation;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;

import static nextstep.subway.line.acceptance.LineSteps.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.path.acceptance.PathSteps.*;
import static nextstep.subway.station.StationSteps.지하철역_등록되어_있음;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathDocumentation extends Documentation {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        super.setUp(restDocumentation);

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10, 10);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10, 10);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5, 10);

        지하철_노선에_지하철역_등록_요청(삼호선, 남부터미널역, 양재역, 5, 15);
    }

    @Test
    void path() {
        RestAssured
                .given(spec).log().all()
                .filter(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        지하철_노선_경로탐색_설명(),
                        지하철_노선_경로탐색_결과_필드()))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 강남역.getId())
                .queryParam("target", 양재역.getId())
                .queryParam("type", "DISTANCE")
                .when().get("/paths")
                .then().log().all().extract();
    }
}


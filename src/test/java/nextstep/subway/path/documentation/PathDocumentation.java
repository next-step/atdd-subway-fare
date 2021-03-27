package nextstep.subway.path.documentation;

import io.restassured.RestAssured;
import nextstep.subway.Documentation;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static nextstep.subway.path.acceptance.PathSteps.지하철_노선_등록되어_있음;
import static nextstep.subway.station.StationSteps.지하철역_등록되어_있음;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathDocumentation extends Documentation {

    @Test
    void path() {
        StationResponse 강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        StationResponse 교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        LineResponse 이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10, 10);

        RestAssured
                .given(spec).log().all()
                .filter(document("path",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestParameters(
                                        parameterWithName("source").description("출발지점 ID"),
                                        parameterWithName("target").description("도착지점 ID"),
                                        parameterWithName("type").description("타입(최단거리/소요시간)")
                                )
                ))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 강남역.getId())
                .queryParam("target", 교대역.getId())
                .queryParam("type", "DISTANCE")
                .when().get("/paths")
                .then().log().all().extract();
    }
}


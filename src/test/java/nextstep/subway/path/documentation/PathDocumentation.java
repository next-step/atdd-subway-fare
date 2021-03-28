package nextstep.subway.path.documentation;

import io.restassured.RestAssured;
import nextstep.subway.Documentation;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static nextstep.subway.path.acceptance.PathSteps.지하철_노선_등록되어_있음;
import static nextstep.subway.station.StationSteps.지하철역_등록되어_있음;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathDocumentation extends Documentation {

    StationResponse 강남역;
    StationResponse 교대역;

    @BeforeEach
    void setUp() {
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10, 10);
    }

    @Test
    void findPath() {
        RestAssured
                .given(spec).log().all()
                .filter(document("path",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("source").description("출발지점 ID"),
                                parameterWithName("target").description("도착지점 ID"),
                                parameterWithName("type").description("타입(최단거리/소요시간) - DISTANCE/DURATION")
                        ),
                        responseFields(
                                fieldWithPath("stations").description("응답"),
                                fieldWithPath("stations[].id").description("역 ID"),
                                fieldWithPath("stations[].name").description("역 이름"),
                                fieldWithPath("stations[].createdDate").description("역이 생성된 시간"),
                                fieldWithPath("stations[].modifiedDate").description("역이 변경된 시간"),
                                fieldWithPath("distance").description("역간 최단거리"),
                                fieldWithPath("duration").description("소요시간"),
                                fieldWithPath("fare").description("지하철 이용 요금")

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


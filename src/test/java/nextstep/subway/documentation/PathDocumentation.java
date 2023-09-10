package nextstep.subway.documentation;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.RestAssured;
import java.util.List;
import nextstep.subway.controller.PathController;
import nextstep.subway.dto.PathRequest;
import nextstep.subway.dto.PathResponse;
import nextstep.subway.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@DisplayName("경로 조회 API 문서")
public class PathDocumentation extends Documentation {

    @MockBean
    private PathController pathController;

    @Test
    @DisplayName("[성공] 경로 조회 요청 문서")
    void 경로_조회_요청_문서() {
        // Given
        Long 교대역 = 1L;
        Long 양재역 = 3L;
        PathResponse 경로_조회_응답 = PathResponse.builder()
            .stations(List.of(
                new StationResponse(교대역, "교대역"),
                new StationResponse(2L, "남부터미널역"),
                new StationResponse(양재역, "양재역")
            ))
            .distance(5L)
            .build();
        Mockito.when(pathController.findPath(Mockito.any(PathRequest.class)))
            .thenReturn(ResponseEntity.ok(경로_조회_응답));

        // When
        RestAssured
            .given(spec).log().all()
            .filter(document("path",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                    requestParameters(
                        parameterWithName("source").description("경로 출발역"),
                        parameterWithName("target").description("경로 도착역")
                        ),
                    responseFields(
                        fieldWithPath("stations").description("경로의 지하철역 리스트"),
                        fieldWithPath("stations.[].id").description("아이디"),
                        fieldWithPath("stations.[].name").description("이름"),
                        fieldWithPath("distance").description("경로의 총 거리")
                    )))
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("source", 교대역)
            .queryParam("target", 양재역)
            .when().get("/paths")
            .then().log().all().extract();
    }

}
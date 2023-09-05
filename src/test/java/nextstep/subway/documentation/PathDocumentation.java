package nextstep.subway.documentation;

import static nextstep.subway.documentation.PathDocumentationSteps.PathInformation;
import static nextstep.subway.documentation.PathDocumentationSteps.경로_조회_요청_문서_데이터_생성;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.RestAssured;
import java.util.List;
import nextstep.subway.controller.PathController;
import nextstep.subway.domain.Station;
import nextstep.subway.dto.PathRequest;
import nextstep.subway.dto.PathResponse;
import nextstep.subway.dto.StationResponse;
import nextstep.subway.service.PathService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class PathDocumentation extends Documentation {

    @MockBean
    private PathController pathController;

    @Test
    @DisplayName("[성공] 경로를 조회 요청 문서")
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
                preprocessResponse(prettyPrint())))
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("source", 교대역)
            .queryParam("target", 양재역)
            .when().get("/paths")
            .then().log().all().extract();
    }

}
package nextstep.subway.documentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import java.util.List;
import nextstep.subway.acceptance.PathSteps;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;

public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @Test
    void path() {
        PathResponse pathResponse = new PathResponse(
            List.of(
                new StationResponse(1L, "강남역"),
                new StationResponse(2L, "역삼역")
            ),
            10,
            10,
            1_250
        );

        when(pathService.findPath(any(), any())).thenReturn(pathResponse);

        PathSteps.두_역의_경로_조회를_요청(given(), 1L,1L, "DISTANCE");
    }

    private RequestSpecification given() {
        return RestAssured
            .given().log().all()
            .filter(
                document(
                    "path",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestParameters(
                        parameterWithName("source").description("출발역"),
                        parameterWithName("target").description("도착역"),
                        parameterWithName("type").description("경로 조회 타입")
                    ),
                    responseFields(
                        fieldWithPath("stations").type(JsonFieldType.ARRAY).description("경로의 역들"),
                        fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("역 아이디"),
                        fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("역 이름"),
                        fieldWithPath("distance").type(JsonFieldType.NUMBER).description("거리"),
                        fieldWithPath("duration").type(JsonFieldType.NUMBER).description("시간"),
                        fieldWithPath("fare").type(JsonFieldType.NUMBER).description("요금(거리비례제로 책정)")
                    )
                )
            );
    }
}

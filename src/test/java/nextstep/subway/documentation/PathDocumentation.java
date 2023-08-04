package nextstep.subway.documentation;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static nextstep.subway.acceptance.PathSteps.두_역의_최단_거리_경로_조회를_요청;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @Test
    void path() {
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "강남역"),
                        new StationResponse(2L, "역삼역")
                ), 10
        );

        when(pathService.findPath(anyLong(), anyLong())).thenReturn(pathResponse);

        RequestSpecification requestSpecification = RestAssured.given(spec).log().all()
                .filter(document("path",
                        requestParameters(
                                parameterWithName("source").description("출발역 아이디"),
                                parameterWithName("target").description("도착역 아이디")
                        ),
                        responseFields(
                                fieldWithPath("stations").description("경로에 포함된 역 목록"),
                                fieldWithPath("stations[].id").description("역 아이디"),
                                fieldWithPath("stations[].name").description("역 이름"),
                                fieldWithPath("distance").description("총 거리")
                        )));

        두_역의_최단_거리_경로_조회를_요청(1L, 2L, requestSpecification);
    }
}

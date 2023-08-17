package nextstep.subway.documentation;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.constant.FindPathType;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static nextstep.subway.acceptance.PathSteps.두_역의_최단_거리_경로_및_소요시간_조회를_요청_문서화;
import static org.mockito.ArgumentMatchers.any;
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
                ),
                10,
                10,
                1250
        );

        when(pathService.findPath(anyLong(), anyLong(), any())).thenReturn(pathResponse);

        RequestSpecification requestSpecification = RestAssured.given(spec).log().all()
                .filter(document("path",
                        requestParameters(
                                parameterWithName("source").description("출발역 아이디"),
                                parameterWithName("target").description("도착역 아이디"),
                                parameterWithName("type").description("경로 탐색 방법")
                        ),
                        responseFields(
                                fieldWithPath("stations").description("경로에 포함된 역 목록"),
                                fieldWithPath("stations[].id").description("역 아이디"),
                                fieldWithPath("stations[].name").description("역 이름"),
                                fieldWithPath("distance").description("총 거리"),
                                fieldWithPath("duration").description("총 소요 시간"),
                                fieldWithPath("fare").description("총 요금")
                        )));

        두_역의_최단_거리_경로_및_소요시간_조회를_요청_문서화(1L, 2L, requestSpecification, FindPathType.DURATION.getType());
    }
}

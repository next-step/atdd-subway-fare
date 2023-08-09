package nextstep.subway.documentation;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static nextstep.subway.acceptance.PathSteps.경로_조회_요청;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
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

        // given
        RequestSpecification requestSpecification = RestAssured.given(spec).log().all()
                .filter(document("path",
                                 preprocessRequest(prettyPrint()),
                                 preprocessResponse(prettyPrint()),
                                 requestParameters(
                                        parameterWithName("source").description("출발역 ID"),
                                        parameterWithName("target").description("도착역 ID"),
                                        parameterWithName("type").description("경로 조회 기준 (DURATION: 소요시간, DISTANCE: 거리)")
                                 ),
                                responseFields(
                                        fieldWithPath("stations[].id").description("지하철역 ID"),
                                        fieldWithPath("stations[].name").description("지하철역 이름"),
                                        fieldWithPath("distance").description("조회된 경로 거리"),
                                        fieldWithPath("duration").description("조회된 경로 소요시간"),
                                        fieldWithPath("fare").description("요금"))
                        )
                );


        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "강남역"),
                        new StationResponse(2L, "역삼역")
                ), 10, 12, 1250);

        when(pathService.findPath(anyLong(), anyLong(), any(), any())).thenReturn(pathResponse);

        경로_조회_요청(requestSpecification, 1L, 2L, "DISTANCE");
    }
}

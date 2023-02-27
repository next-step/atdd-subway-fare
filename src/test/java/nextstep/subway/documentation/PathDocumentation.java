package nextstep.subway.documentation;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.PathType;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static nextstep.subway.acceptance.PathSteps.두_역의_최단_거리_경로_조회를_요청;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
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
        final StationResponse 강남역 = new StationResponse(1L, "강남역");
        final StationResponse 역삼역 = new StationResponse(2L, "역삼역");
        PathResponse pathResponse = new PathResponse(Lists.newArrayList(강남역, 역삼역), 10, 5);

        when(pathService.findPath(anyLong(), anyLong(), eq(PathType.DISTANCE))).thenReturn(pathResponse);

        this.spec.filter(
                document("path",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("source").description("출발역 ID"),
                                parameterWithName("target").description("도착역 ID"),
                                parameterWithName("type").description("경로 조회 기준")
                        ),
                        responseFields(
                                fieldWithPath("stations").description("경로에 있는 지하철역 목록"),
                                fieldWithPath("stations[].id").description("지하철역 ID"),
                                fieldWithPath("stations[].name").description("지하철역 이름"),
                                fieldWithPath("distance").description("거리"),
                                fieldWithPath("duration").description("소요 시간")
                        )
                )
        );

        두_역의_최단_거리_경로_조회를_요청(this.spec, 강남역.getId(), 역삼역.getId(), PathType.DISTANCE);
    }
}

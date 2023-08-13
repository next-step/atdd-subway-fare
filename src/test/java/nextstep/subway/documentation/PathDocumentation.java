package nextstep.subway.documentation;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.PathType;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;

import static nextstep.subway.acceptance.PathSteps.두_역의_최단_시간_경로_조회를_요청;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

@ExtendWith(MockitoExtension.class)
public class PathDocumentation extends Documentation {
    @MockBean
    private PathService pathService;

    @Override
    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        super.setUp(restDocumentation);
        spec.filter(document("path",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParameters(
                        parameterWithName("source").description("출발역 ID"),
                        parameterWithName("target").description("도착역 ID"),
                        parameterWithName("type").description("경로 조회 방식(DISTANCE: 최단 경로, DURATION: 최단 시간)")
                ),
                responseFields(
                        fieldWithPath("stations.[].id").description("역 ID"),
                        fieldWithPath("stations.[].name").description("역 이름"),
                        fieldWithPath("distance").description("지나가는 거리 합"),
                        fieldWithPath("duration").description("총 소요 시간"),
                        fieldWithPath("price").description("요금")
                )));
    }

    @Test
    void path() {
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "강남역"),
                        new StationResponse(2L, "역삼역")
                ), 10, 10, 1250
        );

        when(pathService.findPath(1L, 2L, PathType.DURATION)).thenReturn(pathResponse);

        두_역의_최단_시간_경로_조회를_요청(spec, 1L, 2L);
    }
}

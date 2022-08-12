package nextstep.subway.documentation;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

import static nextstep.subway.acceptance.PathSteps.두_역의_경로_조회를_요청;
import static nextstep.subway.domain.PathType.DISTANCE;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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
        this.spec.filter(document("path",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                경로_요청_정의(),
                경로_응답_정의()
        ));
        두_역의_경로_조회를_요청(1L, 2L, DISTANCE.name(), this.spec);
    }

    private RequestParametersSnippet 경로_요청_정의() {
        return requestParameters(
                parameterWithName("source").description("출발역 Id"),
                parameterWithName("target").description("도착역 Id"),
                parameterWithName("type").description("경로 요청 타입")
        );
    }

    private static ResponseFieldsSnippet 경로_응답_정의() {
        return responseFields(
                subsectionWithPath("stations").description("경로에 거쳐가는 역 목록"),
                fieldWithPath("stations[].id").description("역 Id"),
                fieldWithPath("stations[].name").description("역 이름"),
                fieldWithPath("distance").description("경로 총 거리")
        );
    }
}

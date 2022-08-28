package nextstep.subway.documentation;

import nextstep.subway.acceptance.AcceptanceTestSteps;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.PathType;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;
import support.auth.userdetails.UserDetails;

import static nextstep.subway.acceptance.PathSteps.두_역의_경로_조회를_요청;
import static nextstep.subway.domain.PathType.DISTANCE;
import static nextstep.subway.domain.PathType.DURATION;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @Test
    void pathByDistance() {

        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(new StationResponse(1L, "강남역"), new StationResponse(2L, "역삼역")), 10, 5, 1250);

        when(pathService.findPath(any(), anyLong(), anyLong(), any(UserDetails.class))).thenReturn(pathResponse);

        AcceptanceTestSteps.given(spec, "path", getRequestParametersSnippet(), getResponseFieldsSnippet());

        두_역의_경로_조회를_요청(AcceptanceTestSteps.given(spec, "path", getRequestParametersSnippet(), getResponseFieldsSnippet()), 1L, 2L, DISTANCE);
    }

    @Test
    void pathByDuration() {

        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(new StationResponse(1L, "강남역"), new StationResponse(2L, "역삼역")), 10, 5, 1250);

        when(pathService.findPath(any(PathType.class), anyLong(), anyLong(), any(UserDetails.class))).thenReturn(pathResponse);

        두_역의_경로_조회를_요청(AcceptanceTestSteps.given(spec, "path", getRequestParametersSnippet(), getResponseFieldsSnippet()), 1L, 2L, DURATION);
    }

    private ResponseFieldsSnippet getResponseFieldsSnippet() {
        return responseFields(
                fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("지하철역 ID"),
                fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("지하철역 이름"),
                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("거리"),
                fieldWithPath("duration").type(JsonFieldType.NUMBER).description("경과 시간"),
                fieldWithPath("fare").type(JsonFieldType.NUMBER).description("운행ㄴ 요금")
        );
    }

    private RequestParametersSnippet getRequestParametersSnippet() {
        return requestParameters(
                parameterWithName("type").description("경로 조회 타입"),
                parameterWithName("source").description("출발역 ID"),
                parameterWithName("target").description("도착역 ID")
        );
    }

}

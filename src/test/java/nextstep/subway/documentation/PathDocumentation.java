package nextstep.subway.documentation;

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
import support.auth.userdetails.User;

import java.time.LocalDateTime;

import static nextstep.subway.acceptance.AcceptanceTestSteps.given;
import static nextstep.subway.acceptance.member.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.path.PathSteps.두_역의_최단_거리_경로_조회를_요청;
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
    void path() {
        final LocalDateTime now = LocalDateTime.now();
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "강남역", now, now),
                        new StationResponse(2L, "역삼역", now, now)
                ), 10, 4, 1250
        );
        String accessToken = 로그인_되어_있음("member@email.com", "password");
        when(pathService.findPath(any(User.class), anyLong(), anyLong(), any(PathType.class))).thenReturn(pathResponse);

        두_역의_최단_거리_경로_조회를_요청(given(accessToken, spec, "path", getRequestParametersSnippet(), getResponseFieldsSnippet()), 1L, 2L);
    }

    private ResponseFieldsSnippet getResponseFieldsSnippet() {
        return responseFields(
                fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("지하철역 ID"),
                fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("지하철역 이름"),
                fieldWithPath("stations[].createdDate").type(JsonFieldType.STRING).description("생성일자"),
                fieldWithPath("stations[].modifiedDate").type(JsonFieldType.STRING).description("수정일자"),
                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("거리"),
                fieldWithPath("duration").type(JsonFieldType.NUMBER).description("경과 시간"),
                fieldWithPath("fare").type(JsonFieldType.NUMBER).description("요금")
        );
    }

    private RequestParametersSnippet getRequestParametersSnippet() {
        return requestParameters(
                parameterWithName("source").description("출발역 ID"),
                parameterWithName("target").description("도착역 ID"),
                parameterWithName("type").description("경로 조회 타입")
        );
    }
}

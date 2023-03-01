package nextstep.subway.documentation;

import nextstep.member.application.JwtTokenProvider;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.PathSearchRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import java.util.List;

import static nextstep.subway.acceptance.PathSteps.두_역의_최단_거리_경로_조회를_요청;
import static nextstep.subway.acceptance.PathSteps.두_역의_최소_시간_경로_조회를_요청;
import static nextstep.subway.acceptance.PathSteps.로그인_사용자가_경로_조회를_요청;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

@DisplayName("경로찾기 문서화 테스트")
@ExtendWith(MockitoExtension.class)
public class PathDocumentation extends Documentation {
    @MockBean
    private PathService pathService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void pathLogin() {
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "강남역"),
                        new StationResponse(4L, "양재역")
                ), 10, 10, 1250
        );

        BDDMockito.given(pathService.findPath(any(PathSearchRequest.class), any())).willReturn(pathResponse);
        BDDMockito.given(jwtTokenProvider.getPrincipal(anyString())).willReturn("1");
        BDDMockito.given(jwtTokenProvider.getRoles(anyString())).willReturn(List.of("ROLE_USER"));

        RestDocumentationFilter filter = document("pathLogin",
                requestHeaders(
                        headerWithName("Authorization").description("Bearer 토큰")),
                requestParameters(
                        parameterWithName("source").description("출발역 ID"),
                        parameterWithName("target").description("도착역 ID"),
                        parameterWithName("type").description("검색 조건(Optional)\n기본값: DISTANCE")),
                responseFields(
                        fieldWithPath("distance").description("거리"),
                        fieldWithPath("duration").description("예상 소요 시간"),
                        fieldWithPath("cost").description("할인이 적용된 요금"),
                        fieldWithPath("stations[].id").description("지하철역 ID"),
                        fieldWithPath("stations[].name").description("지하철역 이름")));
        String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI...";
        로그인_사용자가_경로_조회를_요청(accessToken, 1L, 4L, spec, filter);
    }

    @Test
    void pathDefault() {
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "강남역"),
                        new StationResponse(4L, "양재역")
                ), 10, 10, 1250
        );

        BDDMockito.given(pathService.findPath(any(PathSearchRequest.class), any())).willReturn(pathResponse);

        두_역의_최단_거리_경로_조회를_요청(1L, 4L, spec, defaultFilter("pathDefault"));
    }

    @Test
    void pathDuration() {
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "강남역"),
                        new StationResponse(2L, "교대역"),
                        new StationResponse(3L, "남부터미널"),
                        new StationResponse(4L, "양재역")
                ), 12, 5, 1350
        );

        BDDMockito.given(pathService.findPath(any(PathSearchRequest.class), any())).willReturn(pathResponse);

        두_역의_최소_시간_경로_조회를_요청(1L, 4L, spec, defaultFilter("pathDuration"));
    }
}

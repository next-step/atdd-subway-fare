package nextstep.subway.documentation;

import nextstep.member.domain.LoginMember;
import nextstep.member.ui.AuthenticationPrincipalArgumentResolver;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.PathType;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;

import java.util.Collections;

import static nextstep.subway.acceptance.PathSteps.두_역의_최단_거리_경로_조회를_요청_문서화;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
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
    @MockBean
    private AuthenticationPrincipalArgumentResolver argumentResolver;

    @Test
    void path() throws Exception {
        final StationResponse 강남역 = new StationResponse(1L, "강남역");
        final StationResponse 역삼역 = new StationResponse(2L, "역삼역");
        PathResponse pathResponse = new PathResponse(Lists.newArrayList(강남역, 역삼역), 10, 5, 1250);

        final String token = "token";
        when(argumentResolver.resolveArgument(any(), any(), any(), any())).thenReturn(new LoginMember(1L, Collections.emptyList()));
        when(pathService.findPath(any(LoginMember.class), anyLong(), anyLong(), eq(PathType.DISTANCE))).thenReturn(pathResponse);

        this.spec.filter(
                document("path",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName(HttpHeaders.AUTHORIZATION).optional().description("로그인하면 연령별 할인 혜택이 적용될 수 있음")),
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
                                fieldWithPath("duration").description("소요 시간"),
                                fieldWithPath("fare").description("최단 거리 기준 이용 요금")
                        )
                )
        );

        두_역의_최단_거리_경로_조회를_요청_문서화(this.spec, token, 강남역.getId(), 역삼역.getId(), PathType.DISTANCE);
    }
}

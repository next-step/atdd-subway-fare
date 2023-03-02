package nextstep.subway.documentation;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.domain.LoginMember;
import nextstep.member.ui.AuthenticationPrincipalArgumentResolver;
import nextstep.subway.acceptance.PathSteps;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.domain.PathType;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class PathDocumentation extends Documentation {

    @MockBean
    PathService pathService;
    @MockBean
    AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Test
    void path() throws Exception{
        PathResponse pathResponse = new PathResponse(Lists.newArrayList(new StationResponse(1L, "강남역"),
                new StationResponse(2L, "역삼역")),10, 20, 1250L);

        when(pathService.findPath(any(),anyLong(), anyLong(), eq(PathType.시간.getType()))).thenReturn(pathResponse);
        when(authenticationPrincipalArgumentResolver.resolveArgument(any(), any(), any(), any())).thenReturn(new LoginMember(1L, List.of("ROLE_MEMBER")));

        // when
        ExtractableResponse<Response> searchResponse = PathSteps.회원계정으로_두_역의_경로_조회를_요청(spec, "TestAccessToken",1L, 2L, PathType.시간.getType());
        assertThat(searchResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}

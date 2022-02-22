package nextstep.subway.documentation;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.PathSteps;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import java.util.Map;

import static nextstep.subway.acceptance.MemberSteps.*;
import static nextstep.subway.acceptance.PathSteps.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class PathDocumentation extends Documentation {
    public static final String EMAIL = "email@email";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;
    @MockBean
    private PathService pathService;

    @Test
    void path() {
        //given
        PathResponse pathResponse = getPathResponseForAnonymous();
        when(pathService.findPath(anyLong(), anyLong())).thenReturn(pathResponse);
        Map<String, String> params = 경로_조회_파라미터_생성(1L, 2L, "SHORTEST_DISTANCE");
        RestDocumentationFilter filter = PathSteps.경로관련_문서_필터생성("path-anonymous");

        //when
        ExtractableResponse<Response> response = 경로조회_비회원_문서화(spec, filter, params);

        //then
        경로조회_검증됨(response);
    }



    @Test
    void pathForUser() {
        //given
        회원_생성됨(회원_생성_요청(EMAIL, PASSWORD, 14));
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);
        PathResponse pathResponse = getPathResponseForUser();
        when(pathService.findPath(anyLong(), anyLong())).thenReturn(pathResponse);
        Map<String, String> params = 경로_조회_파라미터_생성(1L, 2L, "MINIMUM_TIME");
        RestDocumentationFilter filter = PathSteps.경로관련_문서_필터생성("path-user");

        //when
        ExtractableResponse<Response> response = 경로조회_회원_문서화(accessToken, spec, filter, params);

        //then
        경로조회_검증됨(response);
    }


}

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
import static nextstep.subway.acceptance.PathSteps.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class PathDocumentation extends Documentation {
    @MockBean
    private PathService pathService;

    @Test
    void path() {
        //given
        PathResponse pathResponse = getPathResponse();
        when(pathService.findPath(anyLong(), anyLong())).thenReturn(pathResponse);
        Map<String, String> params = 경로_조회_파라미터_생성();
        RestDocumentationFilter filter = PathSteps.경로관련_문서_필터생성("path");

        //when
        ExtractableResponse<Response> response = 경로조회_문서생성_최단거리_기준(spec, filter, params);

        //then
        경로조회_검증됨(response);
    }



    @Test
    void pathByDuration() {
        //given
        PathResponse pathResponse = getPathResponse();
        when(pathService.findPathByMinimumTime(anyLong(), anyLong())).thenReturn(pathResponse);
        Map<String, String> params = 경로_조회_파라미터_생성();
        RestDocumentationFilter filter = PathSteps.경로관련_문서_필터생성("pathByDuration");

        //when
        ExtractableResponse<Response> response = 경로조회_문서생성_최소시간_기준(spec, filter, params);

        //then
        경로조회_검증됨(response);
    }

    @Test
    void pathByFee() {
        //given
        PathResponse pathResponse = getPathResponse();
        when(pathService.findPathByMinimumFee(anyInt(), anyLong(), anyLong())).thenReturn(pathResponse);
        Map<String, String> params = 경로_조회_파라미터_생성();
        RestDocumentationFilter filter = PathSteps.경로관련_문서_필터생성("pathByFee");

        //when
        ExtractableResponse<Response> response = 경로조회_문서생성_최소금액_거리_기준(spec, filter, params);

        //then
        경로조회_검증됨(response);
    }

}

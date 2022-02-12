package nextstep.subway.documentation;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.PathType;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static nextstep.subway.acceptance.PathSteps.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class PathDocumentation extends Documentation {

    @MockBean
    PathService pathService;

    Long 강남역 = 1L;
    Long 분당역 = 2L;
    int 거리 = 10;
    int 소요시간 = 60;

    @Test
    void path() {
        // given
        when(pathService.findPath(anyLong(), anyLong(), any(PathType.class))).thenReturn(getMockPathResponse());

        // when
        ExtractableResponse<Response> 조회_응답 = 두_역의_최소_시간_경로_조회를_요청_문서화(강남역, 분당역, spec);

        // then
        경로_조회됨(조회_응답, 강남역, 분당역);
        경로_소요시간_조회됨(조회_응답, 소요시간);
    }

    private PathResponse getMockPathResponse() {
        return new PathResponse(
                Lists.newArrayList(
                        new StationResponse(강남역, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                        new StationResponse(분당역, "분당역", LocalDateTime.now(), LocalDateTime.now()))
                , 거리
                , 소요시간);
    }
}

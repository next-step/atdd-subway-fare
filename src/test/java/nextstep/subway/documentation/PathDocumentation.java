package nextstep.subway.documentation;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import static nextstep.subway.acceptance.PathSteps.*;
import static org.assertj.core.api.Assertions.assertThat;
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

        RestDocumentationFilter filter = 경로관련_문서_필터생성();
        Map<String, String> params = 경로_조회_파라미터_생성();

        //when
        ExtractableResponse<Response> response = 경로조회_및_문서_생성(spec, filter, params);

        //then
        경로조회_검증됨(response);
    }

    private PathResponse getPathResponse() {
        PathResponse pathResponse = new PathResponse(
                Arrays.asList(
                        new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                        new StationResponse(2L, "역삼역", LocalDateTime.now(), LocalDateTime.now())
                ), 10
        );
        return pathResponse;
    }

    private void 경로조회_검증됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("stations.id")).containsExactly(1, 2);
        assertThat(response.jsonPath().getList("stations.name")).containsExactly("강남역", "역삼역");
    }


}

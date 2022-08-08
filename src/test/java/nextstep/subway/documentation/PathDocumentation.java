package nextstep.subway.documentation;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class PathDocumentation extends Documentation {
    @MockBean
    private PathService pathService;

    @Test
    void path() {
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "강남역"),
                        new StationResponse(2L, "역삼역")
                ), 10, 10, 1250
        );

        when(pathService.findPath(anyLong(), anyLong(), any())).thenReturn(pathResponse);

        var response = Path_경로조회_요청_최소시간(1L, 2L);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath()
                .getInt("distance")).isEqualTo(10);
    }

    public ExtractableResponse<Response> Path_경로조회_요청_최소시간(Long sourceStationId, Long targetStationId) {
        return PATH_GIVEN_SPEC설정_filter설정()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", sourceStationId)
                .queryParam("target", targetStationId)
                .queryParam("type", "DURATION")
                .when()
                .get("/paths")
                .then()
                .log()
                .all()
                .extract();
    }
}

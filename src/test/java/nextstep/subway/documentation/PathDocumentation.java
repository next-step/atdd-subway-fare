package nextstep.subway.documentation;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static nextstep.subway.documentation.PathSteps.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@DisplayName("경로 관리(문서화)")
public class PathDocumentation extends Documentation {
    @MockBean
    private PathService pathService;

    @DisplayName("경로 찾기 요청 - 거리")
    @Test
    void pathByDistance() {
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                        new StationResponse(2L, "역삼역", LocalDateTime.now(), LocalDateTime.now())
                ),
                10,
                7,
                1250
        );

        when(pathService.findPath(anyLong(), anyLong(), any())).thenReturn(pathResponse);

        ExtractableResponse<Response> 최단_경로_요청 = 최단_거리_경로_요청(spec, 1L, 2L);

        assertThat(최단_경로_요청.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("경로 찾기 요청 - 시간")
    @Test
    void pathByDuration() {
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                        new StationResponse(2L, "역삼역", LocalDateTime.now(), LocalDateTime.now())
                ),
                10,
                7,
                1250
        );

        when(pathService.findPath(anyLong(), anyLong(), any())).thenReturn(pathResponse);

        ExtractableResponse<Response> 최단_경로_요청 = 최단_시간_경로_요청(spec, 1L, 2L);

        assertThat(최단_경로_요청.statusCode()).isEqualTo(HttpStatus.OK.value());
    }


    @DisplayName("경로 찾기 요청 - 요금")
    @CsvSource(value = {"1,1250","10:1250","11:1350","20:1550"}, delimiter = ':')
    @ParameterizedTest
    void pathByFare(int distance, int fare) {
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                        new StationResponse(2L, "역삼역", LocalDateTime.now(), LocalDateTime.now())
                ),
                distance,
                7,
                fare
        );

        when(pathService.findPath(anyLong(), anyLong(), any())).thenReturn(pathResponse);

        ExtractableResponse<Response> 최단_경로_요청 = 최단_시간_경로_요청(spec, 1L, 2L);

        Assertions.assertAll(
                () -> assertThat(최단_경로_요청.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(최단_경로_요청.jsonPath().getInt("fare")).isEqualTo(fare)
        );
    }
}

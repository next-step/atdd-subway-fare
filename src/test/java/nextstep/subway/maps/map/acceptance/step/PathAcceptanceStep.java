package nextstep.subway.maps.map.acceptance.step;

import com.google.common.collect.Lists;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.maps.map.dto.PathResponse;
import nextstep.subway.maps.station.dto.StationResponse;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class PathAcceptanceStep {
    public static ExtractableResponse<Response> 출발역에서_도착역까지의_최단_혹은_최소시간_거리_경로_조회_요청(String type) {
        return RestAssured.given().log().all().
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/paths?source={sourceId}&target={targetId}&type={type}", 1L, 3L, type).
                then().
                log().all().
                extract();
    }

    public static void 적절한_경로를_응답(ExtractableResponse<Response> response, long l) {
        PathResponse pathResponse = response.as(PathResponse.class);
        List<Long> stationIds = pathResponse.getStations().stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(Lists.newArrayList(1L, l, 3L));
    }

    public static void 총_거리와_소요_시간을_함께_응답함(ExtractableResponse<Response> response, int i, int i2) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(i);
        assertThat(pathResponse.getDuration()).isEqualTo(i2);
    }

    public static void 지하철_이용_요금도_함께_응답함(ExtractableResponse<Response> response) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getFare()).isNotNull();
    }
}

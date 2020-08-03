package nextstep.subway.maps.map.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.maps.map.domain.PathType;
import nextstep.subway.maps.map.dto.PathResponse;
import nextstep.subway.maps.station.dto.StationResponse;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class PathAcceptanceStep {
    public static ExtractableResponse<Response> 최단_경로를_조회한다(long source, long target, PathType duration) {
        return RestAssured.given().log().all().
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/paths?source={sourceId}&target={targetId}&type={type}", source, target, duration).
                then().
                log().all().
                extract();
    }

    public static ExtractableResponse<Response> 도착시간이_가장_빠른_경로를_조회한다(Long source, Long target, LocalDateTime time) {
        String formattedTime = time.format(DateTimeFormatter.ofPattern("yyyyMMddhhmm"));
        return RestAssured.given().log().all().
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/paths?source={sourceId}&target={targetId}&type={type}&time={time}", source, target, PathType.ARRIVAL_TIME, formattedTime).
                then().
                log().all().
                extract();
    }

    public static void 경로가_정상적으로_조회됨(ExtractableResponse<Response> response, int distance, int duration, int fare, List<Long> expectedStations) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(distance);
        assertThat(pathResponse.getDuration()).isEqualTo(duration);
        assertThat(pathResponse.getFare()).isEqualTo(fare);
        List<Long> stationIds = pathResponse.getStations().stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStations);
    }
}

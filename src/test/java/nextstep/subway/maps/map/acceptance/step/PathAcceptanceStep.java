package nextstep.subway.maps.map.acceptance.step;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.maps.map.dto.FarePathResponse;
import nextstep.subway.maps.station.dto.StationResponse;

public class PathAcceptanceStep {

    public static void 총_거리와_소요시간을_함께_응답검증(ExtractableResponse<Response> response, int distance, int duration) {
        FarePathResponse pathResponse = response.as(FarePathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(distance);
        assertThat(pathResponse.getDuration()).isEqualTo(duration);
    }

    public static void 지하철_이용요금이_함께_응답검증(ExtractableResponse<Response> response, int expectedFare) {
        FarePathResponse farePathResponse = response.as(FarePathResponse.class);
        assertThat(farePathResponse.getFare()).isEqualTo(expectedFare);
    }

    public static void 경로를_순서대로_정렬하여_응답검증(ExtractableResponse<Response> response, List<Long> expectedIds) {
        FarePathResponse pathResponse = response.as(FarePathResponse.class);
        List<Long> stationIds = pathResponse.getStations().stream()
            .map(StationResponse::getId)
            .collect(Collectors.toList());
        assertThat(stationIds).containsExactlyElementsOf(expectedIds);
    }

    public static ExtractableResponse<Response> 출발역에서_도착역까지_최단_또는_최소시간_경로조회_요청(Long sourceStation, Long targetStation,
        String type) {
        return RestAssured.given().log().all().
            accept(MediaType.APPLICATION_JSON_VALUE).
            when().
            get("/paths?source={sourceId}&target={targetId}&type={type}", sourceStation, targetStation, type).
            then().
            log().all().
            extract();
    }

    public static ExtractableResponse<Response> 로그인한_사용자_출발역에서_도착역까지_최단_또는_최소시간_경로조회_요청(Long sourceStation,
        Long targetStation, String type, TokenResponse tokenResponse) {
        return RestAssured.given().log().all().
            auth().oauth2(tokenResponse.getAccessToken()).
            accept(MediaType.APPLICATION_JSON_VALUE).
            when().
            get("/paths?source={sourceId}&target={targetId}&type={type}", sourceStation, targetStation, type).
            then().
            log().all().
            extract();
    }
}

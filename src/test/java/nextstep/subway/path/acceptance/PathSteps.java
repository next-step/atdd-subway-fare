package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.line.acceptance.LineSteps.지하철_노선_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class PathSteps {
    public static LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance, int duration) {
        LineRequest lineRequest = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance, duration);
        return 지하철_노선_생성_요청(lineRequest).as(LineResponse.class);
    }

    public static RequestSpecification given() {
        return RestAssured
                .given().log().all();
    }

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(RequestSpecification given, TokenResponse token, Long source, Long target) {
        return getPaths(given, token, source, target, "DISTANCE");
    }

    public static ExtractableResponse<Response> 두_역의_최소_소요_시간_경로_조회를_요청(RequestSpecification given, TokenResponse token, Long source, Long target) {
        return getPaths(given, token, source, target, "DURATION");
    }

    private static ExtractableResponse<Response> getPaths(RequestSpecification given, TokenResponse token, Long source, Long target, String distance) {
        return given
                .auth().oauth2(token.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("type", distance)
                .when().get("/paths")
                .then().log().all().extract();
    }

    public static void 경로_응답됨(ExtractableResponse<Response> response, List<Long> expectedStationIds, int distance, int duration) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(distance);
        assertThat(pathResponse.getDuration()).isEqualTo(duration);

        List<Long> stationIds = pathResponse.getStations().stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }

    public static void 경로_응답_요금포함(ExtractableResponse<Response> response, ArrayList<Long> expectedStationIds, int distance, int duration, int fare) {
        경로_응답됨(response, expectedStationIds, distance, duration);
        assertThat(response.as(PathResponse.class).getFare()).isEqualTo(fare);
    }
}

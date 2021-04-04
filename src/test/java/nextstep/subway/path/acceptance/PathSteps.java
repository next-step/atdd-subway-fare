package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.line.acceptance.LineSteps.지하철_노선_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class PathSteps {
    public static LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation,
        StationResponse downStation, int distance, int duration, int charge) {
        LineRequest lineRequest = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance, duration, charge);
        return 지하철_노선_생성_요청(lineRequest).as(LineResponse.class);
    }

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target) {
        final RequestSpecification specification = RestAssured.given().log().all();
        return 두_역의_최단_거리_경로_조회를_요청(specification, source, target);
    }

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(
        RequestSpecification requestSpecification, Long source, Long target) {
        return requestSpecification
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("type", "DISTANCE")
                .when().get("/paths")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target, String accessToken) {
        final RequestSpecification specification = RestAssured.given().log().all();
        return 두_역의_최단_거리_경로_조회를_요청(specification, source, target, accessToken);
    }

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(
        RequestSpecification requestSpecification, Long source, Long target, String accessToken) {
        return requestSpecification
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(accessToken)
            .queryParam("source", source)
            .queryParam("target", target)
            .queryParam("type", "DISTANCE")
            .when().get("/paths")
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 두_역의_최소_소요_시간_경로_조회를_요청(Long source, Long target) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("type", "DURATION")
                .when().get("/paths")
                .then().log().all().extract();
    }

    public static void 경로_응답됨(ExtractableResponse<Response> response, List<Long> expectedStationIds, int distance,
        int duration, int fare) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(distance);
        assertThat(pathResponse.getDuration()).isEqualTo(duration);
        assertThat(pathResponse.getFare()).isEqualTo(fare);

        List<Long> stationIds = pathResponse.getStations().stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }
}

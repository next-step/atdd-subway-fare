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

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.line.acceptance.LineSteps.지하철_노선_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathSteps {

    public static LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance, int duration) {
        LineRequest lineRequest = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance, duration);
        return 지하철_노선_생성_요청(lineRequest).as(LineResponse.class);
    }

    public static LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance, int duration, int additionalLineFare) {
        LineRequest lineRequest = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance, duration, additionalLineFare);
        return 지하철_노선_생성_요청(lineRequest).as(LineResponse.class);
    }

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target) {
        return sendRequestWithType(RestAssured.given().log().all(), source, target, "DISTANCE");
    }

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(TokenResponse token, Long source, Long target) {
        return sendRequestWithType(RestAssured.given().log().all().auth().oauth2(token.getAccessToken()), source, target, "DISTANCE");
    }

    public static ExtractableResponse<Response> 두_역의_최소_소요_시간_경로_조회를_요청(Long source, Long target) {
        return sendRequestWithType(RestAssured.given().log().all(), source, target, "DURATION");
    }

    public static ExtractableResponse<Response> 두_역의_최소_소요_시간_경로_조회를_요청(TokenResponse token, Long source, Long target) {
        return sendRequestWithType(RestAssured.given().log().all().auth().oauth2(token.getAccessToken()), source, target, "DURATION");
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

    public static void 경로_응답됨(ExtractableResponse<Response> response, List<Long> expectedStationIds, int distance, int duration, int fare) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(distance);
        assertThat(pathResponse.getDuration()).isEqualTo(duration);
        assertThat(pathResponse.getFare()).isEqualTo(fare);

        List<Long> stationIds = pathResponse.getStations().stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }

    public static ExtractableResponse<Response> 최단경로_조회_문서화(RequestSpecification spec) {
        return sendRequestWithType(setFilter(spec, "shortestPath"), 1L, 2L, "DISTANCE");
    }

    public static ExtractableResponse<Response> 최소시간경로_조회_문서화(RequestSpecification spec) {
        return  sendRequestWithType(setFilter(spec, "fastestPath"), 1L, 2L, "DURATION");

    }

    private static RequestSpecification setFilter(RequestSpecification spec, String identifier) {
        return RestAssured
                .given(spec).log().all()
                .filter(document(identifier,
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("source").description("출발역"),
                                parameterWithName("target").description("도착역"),
                                parameterWithName("type").description("최단 경로 검색 조건"))
                ));
    }

    private static ExtractableResponse<Response> sendRequestWithType(RequestSpecification spec, Long source, Long target, String type) {
        return spec.accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("type", type)
                .when().get("/paths")
                .then().log().all(true)
                .extract();
    }
}

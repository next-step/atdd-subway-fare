package nextstep.subway.acceptance.path;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.dto.PathResponse;
import nextstep.subway.dto.StationResponse;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.acceptance.station.StationTestUtils.지하철_아이디_획득;
import static org.assertj.core.api.Assertions.*;

public class PathTestUtils {

    private PathTestUtils() {}

    public static ExtractableResponse<Response> 지하철_최단_경로_조회(String 출발역_URL, String 도착역_URL, RequestSpecification spec) {
        RequestSpecification given = RestAssured.given(spec).log().all();
        return 지하철_최단_경로_조회_요청(출발역_URL, 도착역_URL, given);
    }

    public static ExtractableResponse<Response> 지하철_최단_경로_조회(String 출발역_URL, String 도착역_URL) {
        RequestSpecification given = RestAssured.given().log().all();
        return 지하철_최단_경로_조회_요청(출발역_URL, 도착역_URL, given);
    }

    private static ExtractableResponse<Response> 지하철_최단_경로_조회_요청(String 출발역_URL, String 도착역_URL, RequestSpecification requestSpecification) {
        return requestSpecification
                .accept(ContentType.JSON)
                .queryParam("source", 지하철_아이디_획득(출발역_URL))
                .queryParam("target", 지하철_아이디_획득(도착역_URL))
                .when()
                .get("/paths")
                .then().log().all()
                .extract();
    }

    public static void 최단_경로_길이는_다음과_같다(ExtractableResponse<Response> extractableResponse, long distance) {
        assertThat(getPathResponse(extractableResponse).getDistance()).isEqualTo(distance);
    }

    public static void 경로_조회_결과는_다음과_같다(ExtractableResponse<Response> extractableResponse, String... 지하철역_URLs) {
        List<Long> idList = Arrays.stream(지하철역_URLs)
                .map(url -> 지하철_아이디_획득(url))
                .collect(Collectors.toList());

        Arrays.stream(지하철역_URLs).collect(Collectors.toList());
        assertThat(getPathResponse(extractableResponse).getStations().stream().map(StationResponse::getId))
                .containsExactlyElementsOf(idList);
    }

    private static PathResponse getPathResponse(ExtractableResponse<Response> extractableResponse) {
        return extractableResponse.body().as(PathResponse.class);
    }
}

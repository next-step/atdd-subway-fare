package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class PathSteps {
    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target) {
        RequestSpecification specification = RestAssured.given().log().all();
        return 두_역의_최단_거리_경로_조회를_요청(source, target, specification);
    }

    public static ExtractableResponse<Response> 두_역의_최소_시간_경로_조회를_요청(Long source, Long target) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("type", "DURATION")
                .when().get("/paths")
                .then().log().all().extract();
    }


    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target, RequestSpecification spec) {
        return spec
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("type", "DISTANCE")
                .when().get("/paths")
                .then().log().all().extract();
    }

    public static void 두_역의_최소_시간_경로_조회_응답됨(ExtractableResponse<Response> response, int distance, int duration, Long... stationIds) {
        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(stationIds);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(distance);
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(duration);
    }
}

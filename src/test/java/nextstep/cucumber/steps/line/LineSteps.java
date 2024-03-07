package nextstep.cucumber.steps.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.line.LineResponse;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class LineSteps {
    public static ExtractableResponse<Response> 노선_생성_요청(String name, Long upstationId, Long downstationId, int distance, int duration, int extraFare) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("upstationId", upstationId.toString());
        params.put("downstationId", downstationId.toString());
        params.put("color", "red");
        params.put("distance", Integer.toString(distance));
        params.put("duration", duration + "");
        params.put("extraFare", extraFare + "");

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선_생성_요청2(String name, Long upstationId, Long downstationId, int distance, int duration, int extraFare,
                                                         String firstDepartureTime, String lastDepartureTime, int intervalTime) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("upstationId", upstationId.toString());
        params.put("downstationId", downstationId.toString());
        params.put("color", "red");
        params.put("distance", Integer.toString(distance));
        params.put("duration", duration + "");
        params.put("extraFare", extraFare + "");
        params.put("firstDepartureTime", firstDepartureTime);
        params.put("lastDepartureTime", lastDepartureTime);
        params.put("intervalTime", intervalTime+"");

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선_조회(Long id) {
        return RestAssured.given().log().all()
                .pathParam("id", id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{id}")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선_목록_조회() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();
    }
}

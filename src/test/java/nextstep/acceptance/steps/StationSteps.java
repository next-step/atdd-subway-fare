package nextstep.acceptance.steps;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class StationSteps extends AcceptanceTestSteps {
    public static ExtractableResponse<Response> 지하철역_생성_요청(String token, String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return given(token)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_삭제_요청(String token, Long id) {
        return given(token)
                .delete("/stations/{id}", id)
                .then().log().all()
                .extract();
    }
}

package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.HashMap;
import java.util.Map;

public class PathSteps {
    public static ExtractableResponse<Response> searchPath(RequestSpecification spec, long source, long target) {
        Map<String, Long> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);

        return RestAssured
                .given(spec).log().all()
                .params(params)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }
}

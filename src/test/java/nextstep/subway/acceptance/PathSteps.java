package nextstep.subway.acceptance;

import static nextstep.subway.documentation.Documentation.restDocsFilter;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class PathSteps {

    public static Map<String, String> dataSetup(Long 출발역_id, Long 도착역_id) {
        Map<String, String> params = new HashMap<>();
        params.put("source", String.valueOf(출발역_id));
        params.put("target", String.valueOf(도착역_id));
        return params;
    }

    public static ExtractableResponse<Response> Path_조회_API(RequestSpecification spec, Long 출발역_ID, Long 도착역_ID) {
        return RestAssured
            .given(spec).log().all()
            .filter(restDocsFilter("path"))
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .params(dataSetup(출발역_ID, 도착역_ID))
            .when().get("/paths")
            .then().log().all().extract();
    }
}

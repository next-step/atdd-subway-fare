package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

import java.util.Map;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathSteps {
    public static ExtractableResponse<Response> 경로_조회_요청(RequestSpecification spec, Long source, Long target) {
        return RestAssured
                .given(spec).log().all()
                .filter(document("path", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .params(Map.of("source", source, "target", target))
                .when().get("/paths")
                .then().log().all()
                .extract();
    }
}

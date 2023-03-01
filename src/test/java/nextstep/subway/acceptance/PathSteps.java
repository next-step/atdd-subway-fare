package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

public class PathSteps {
    public static ExtractableResponse<Response> 경로_찾기_요청(final Long source, final Long target) {
        return 경로_찾기_문서화(RestAssured.given(), null, source, target);
    }

    public static ExtractableResponse<Response> 경로_찾기_문서화(
            final RequestSpecification spec,
            final RestDocumentationFilter document,
            final Long source,
            final Long target
    ) {
        RequestSpecification given = RestAssured
                .given(spec).log().all();

        if (document != null) {
            given = given.filter(document);
        }

        return given
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .when().get("/paths")
                .then().log().all().extract();
    }
}

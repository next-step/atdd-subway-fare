package nextstep.subway.acceptance;

import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;
import org.springframework.restdocs.request.RequestParametersSnippet;

public class PathSteps {

    public static ExtractableResponse<Response> 경로_조회_요청(RequestSpecification spec,
        RequestParametersSnippet requestParametersSnippet, Long source,
        Long target) {

        return RestAssured
            .given(spec).log().all()
            .filters(document("path", requestParametersSnippet))
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("source", source)
            .queryParam("target", target)
            .when().get("/paths")
            .then().log().all()
            .extract();
    }

}

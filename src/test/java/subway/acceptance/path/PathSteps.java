package subway.acceptance.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class PathSteps {
    public static ExtractableResponse<Response> getShortestPath(long sourceId, long targetId) {
        UriComponents retrieveQueryWithBaseUri = UriComponentsBuilder
                .fromUriString("/path")
                .queryParam("source", sourceId)
                .queryParam("target", targetId)
                .build();
        return RestAssured.given().log().all()
                .when().get(retrieveQueryWithBaseUri.toUri())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> getShortestPathForDocument(long sourceId,
                                                                           long targetId,
                                                                           RequestSpecification spec,
                                                                           RestDocumentationFilter filter) {
        UriComponents retrieveQueryWithBaseUri = UriComponentsBuilder
                .fromUriString("/path")
                .queryParam("source", sourceId)
                .queryParam("target", targetId)
                .build();
        return RestAssured.given(spec).log().all()
                .filter(filter)
                .when().get(retrieveQueryWithBaseUri.toUri())
                .then().log().all()
                .extract();
    }


}

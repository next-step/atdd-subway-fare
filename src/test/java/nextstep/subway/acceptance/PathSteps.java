package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

public class PathSteps {
    private PathSteps() {
    }

    public static void 지하철_경로_조회(RequestSpecification specification, RestDocumentationFilter filter, Long sourceId, Long targetId) {
        RestAssured
                .given(specification).log().all()
                .filter(filter)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", sourceId)
                .queryParam("target", targetId)
                .when().get("/paths")
                .then().log().all().extract();
    }
}

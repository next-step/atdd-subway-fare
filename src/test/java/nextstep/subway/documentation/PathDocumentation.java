package nextstep.subway.documentation;

import io.restassured.RestAssured;
import nextstep.marker.Documentation;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

@Documentation
class PathDocumentation {

    @Test
    void path() {
        RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 1L)
                .queryParam("target", 2L)
                .when().get("/paths")
                .then().log().all().extract();
    }
}

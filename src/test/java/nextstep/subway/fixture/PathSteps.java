package nextstep.subway.fixture;

import io.restassured.RestAssured;
import nextstep.path.ui.PathType;
import nextstep.path.ui.PathsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class PathSteps {

    public static PathsResponse getPath(long source, long target) {
        // paths?source=1&target=3
        return RestAssured.given().log().all()
                .when()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("type", PathType.DISTANCE)
                .get("/paths")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(PathsResponse.class);
    }

    public static PathsResponse getPath(long source, long target, PathType type) {
        // paths?source=1&target=3
        return RestAssured.given().log().all()
                .when()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("type", type)
                .get("/paths")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(PathsResponse.class);
    }


}

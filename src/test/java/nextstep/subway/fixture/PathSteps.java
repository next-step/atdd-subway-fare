package nextstep.subway.fixture;

import io.restassured.RestAssured;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.fixture.MemberSteps;
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
        MemberSteps.회원_생성_요청("email@naver.com", "!23123", 20);
        String token = MemberSteps.토큰_생성("email@naver.com", "!23123");

        return RestAssured.given().log().all()
                .auth().oauth2(token)
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

    public static PathsResponse getPath(long source, long target, PathType type, String authToken) {
        // paths?source=1&target=3
        return RestAssured.given().log().all()
                .auth().oauth2(authToken)
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

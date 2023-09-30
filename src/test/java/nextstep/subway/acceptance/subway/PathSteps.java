package nextstep.subway.acceptance.subway;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import static nextstep.subway.documentation.path.PathDocumentationSteps.경로_조회_요청_문서화;

public class PathSteps {

    private PathSteps() {}

    public static ExtractableResponse<Response> 경로_조회_요청(Long source, Long target) {
        return 경로_조회_요청_문서화(new RequestSpecBuilder().build(), source, target);
    }

    public static ExtractableResponse<Response> 최단시간_경로_조회_요청(Long source, Long target, String type) {
        return RestAssured
                .given().log().all()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .get("/paths?source={sourceId}&target={targetId}&type={type}", source, target, type)
                .then().log().all()
                .extract();
    }
}
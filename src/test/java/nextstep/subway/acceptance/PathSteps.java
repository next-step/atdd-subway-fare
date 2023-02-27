package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.domain.PathType;
import org.springframework.http.MediaType;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathSteps {


    public static RequestSpecification baseDocumentRequest(RequestSpecification spec, String directory) {
        return RestAssured
            .given(spec).log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .filter(document("path" + directory,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }

    private static RequestSpecification baseRequest() {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE);
    }

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target) {
        return baseRequest()
            .when().get("/paths?source={sourceId}&target={targetId}&type={pathType}", source, target, PathType.DISTANCE)
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 두_역의_최소_시간_경로_조회를_요청(Long source, Long target) {
        return baseRequest()
            .when().get("/paths?source={sourceId}&target={targetId}&type={pathType}", source, target, PathType.DURATION)
            .then().log().all().extract();
    }
}

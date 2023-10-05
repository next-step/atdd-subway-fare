package nextstep.subway.documentation.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

public class PathDocumentationSteps {

    private PathDocumentationSteps() {}

    public static ExtractableResponse<Response> 최단시간_경로_조회_요청_문서화(RequestSpecification getPathSpec,
                                                             Long sourceId,
                                                             Long targetId,
                                                             String type) {
        return RestAssured
                .given(getPathSpec).log().all()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .get("/paths?source={sourceId}&target={targetId}&type={type}", sourceId, targetId, type)
                .then().log().all()
                .extract();
    }
}
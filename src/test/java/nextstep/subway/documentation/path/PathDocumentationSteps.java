package nextstep.subway.documentation.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

public class PathDocumentationSteps{

    private PathDocumentationSteps() {}

    public static ExtractableResponse<Response> 경로_조회_문서_요청(RequestSpecification getPathSpec,
                                                            Long sourceId,
                                                            Long targetId) {
        return RestAssured
                .given(getPathSpec).log().all()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .get("/paths?source={sourceId}&target={targetId}", sourceId, targetId)
                .then().log().all()
                .extract();
    }
}
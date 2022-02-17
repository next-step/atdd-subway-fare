package nextstep.subway.documentation;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathSteps {
    private static final String DISTANCE_TYPE = "distance";
    private static final String DURATION_TYPE = "duration";

    public static ExtractableResponse<Response> 최단_거리_경로_요청(RequestSpecification spec, Long source, Long target) {
        return 최단_경로_요청(spec, source, target, DISTANCE_TYPE);
    }

    public static ExtractableResponse<Response> 최단_시간_경로_요청(RequestSpecification spec, Long source, Long target) {
        return 최단_경로_요청(spec, source, target, DURATION_TYPE);
    }

    private static ExtractableResponse<Response> 최단_경로_요청(RequestSpecification spec, Long source, Long target, String type) {
        return RestAssured
                .given(spec).log().all()
                .filter(document("path",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("type", type)
                .when().get("/paths")
                .then().log().all().extract();
    }
}

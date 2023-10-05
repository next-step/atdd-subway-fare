package nextstep.subway.acceptance.subway;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import static nextstep.subway.documentation.path.PathDocumentationSteps.최단시간_경로_조회_요청_문서화;

public class PathSteps {

    private PathSteps() {}

    public static ExtractableResponse<Response> 최단시간_경로_조회_요청(Long source, Long target, String type) {
        return 최단시간_경로_조회_요청_문서화(new RequestSpecBuilder().build(), source, target, type);
    }
}
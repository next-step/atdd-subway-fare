package nextstep.subway.acceptance.subway;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import static nextstep.subway.documentation.path.PathDocumentationSteps.경로_조회_요청_문서화;

public class PathAcceptanceSteps {

    private PathAcceptanceSteps() {}

    public static ExtractableResponse<Response> 경로_조회_요청(Long source, Long target) {
        return 경로_조회_요청_문서화(new RequestSpecBuilder().build(), source, target);
    }
}
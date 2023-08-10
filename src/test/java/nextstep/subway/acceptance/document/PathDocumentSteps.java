package nextstep.subway.acceptance.document;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.domain.PathFindType;
import org.springframework.http.MediaType;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

public class PathDocumentSteps {
    public static ExtractableResponse<Response> 두_역의_최단_경로_조회를_요청(
        Long source,
        Long target,
        PathFindType type,
        RequestSpecification spec,
        RestDocumentationFilter filter
    ) {
        return 두_역의_최단_경로_조회를_요청(source, target, type, spec, filter);
    }
}

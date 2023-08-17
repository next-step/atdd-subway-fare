package nextstep.subway.acceptance.document;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.acceptance.PathSteps;
import nextstep.subway.domain.PathFindType;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

public class PathDocumentSteps {
    public static ExtractableResponse<Response> 두_역의_최단_경로_조회를_요청(
        Long source,
        Long target,
        PathFindType type,
        String token,
        RequestSpecification spec,
        RestDocumentationFilter filter
    ) {
        return PathSteps.두_역의_최단_경로_조회를_요청(source, target, type, token, spec, filter);
    }
}

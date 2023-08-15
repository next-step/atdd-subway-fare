package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.domain.PathFindType;
import org.springframework.http.MediaType;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

public class PathSteps {

    public static ExtractableResponse<Response> 두_역의_최단_경로_조회를_요청(
        Long source,
        Long target,
        PathFindType type
    ) {
        return 두_역의_최단_경로_조회를_요청(source, target, type, null, null, null);
    }

    public static ExtractableResponse<Response> 두_역의_최단_경로_조회를_요청(
        Long source,
        Long target,
        PathFindType type,
        String token
    ) {
        return 두_역의_최단_경로_조회를_요청(source, target, type, token, null, null);
    }

    public static ExtractableResponse<Response> 두_역의_최단_경로_조회를_요청(
        Long source,
        Long target,
        PathFindType type,
        String token,
        RequestSpecification spec,
        RestDocumentationFilter filter
    ) {
        RequestSpecification request = RestAssured
            .given().log().all()
            .params("source", source, "target", target, "type", type.name());

        if (token != null) {
            request = request.auth().oauth2(token);
        }

        if (spec != null) {
            request = request.spec(spec);
        }

        if (filter != null) {
            request = request.filter(filter);
        }

        return request
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/paths")
            .then().log().all().extract();
    }
}

package nextstep.subway.documentation;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import java.util.Map;

import static nextstep.subway.acceptance.PathAcceptanceSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

public class PathDocumentationSteps {

    public static ExtractableResponse<Response> 경로_조회_요청(RequestSpecification specification,
                                                         RestDocumentationFilter filter,
                                                         Map<String, Object> parameters) {

        RequestSpecification requestSpecification = given(specification, filter);
        RequestSpecification given = givenWithJsonParameters(requestSpecification, parameters);
        Response when = when(given);
        return then(when);
    }

    private static RequestSpecification given(RequestSpecification specification, RestDocumentationFilter filter) {
        return RestAssured
                .given(specification).log().all()
                .filter(filter);
    }

    public static void 경로_조회_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}

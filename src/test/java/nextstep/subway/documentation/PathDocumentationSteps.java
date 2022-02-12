package nextstep.subway.documentation;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class PathDocumentationSteps {

    private static final String PATH_URI = "/paths";

    public static ExtractableResponse<Response> 경로_조회_요청(RequestSpecification specification,
                                                         RestDocumentationFilter filter,
                                                         Map<String, ?> params) {
        return RestAssured
                .given(specification).log().all()
                .filter(filter)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParams(params)
                .when().get(PATH_URI)
                .then().log().all().extract();
    }

    public static void 경로_조회_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}

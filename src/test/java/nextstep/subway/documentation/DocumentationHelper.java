package nextstep.subway.documentation;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.domain.PathType;
import org.springframework.http.MediaType;

import static nextstep.subway.documentation.DocumentationFilterTemplate.경로_조회_템플릿;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.OK;

public final class DocumentationHelper {

    public static ExtractableResponse<Response> 경로_조회_요청(RequestSpecification spec, PathType pathType) {
        return RestAssured
                .given(spec).log().all()
                .filter(경로_조회_템플릿())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 1L)
                .queryParam("target", 2L)
                .queryParam("pathType", pathType)
                .when().get("/paths")
                .then().log().all().extract();
    }

    public static void 경로_조회_성공(ExtractableResponse<Response> response) {
        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(OK.value());
            assertThat(response.jsonPath().getList("stations")).hasSize(2);
            assertThat(response.jsonPath().getInt("distance")).isEqualTo(10);
            assertThat(response.jsonPath().getInt("duration")).isEqualTo(10);
        });
    }
}

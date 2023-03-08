package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathSteps {

    private static final String DISTANCE_TYPE = "DISTANCE";

    public static RequestSpecification basicDocumentRequest(final RequestSpecification spec, final String path) {
        return RestAssured
                .given(spec).log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .filter(document(path,
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(final Long source,
                                                                     final Long target) {
        return 두_역의_경로_조회_요청(source, target, DISTANCE_TYPE);
    }

    private static ExtractableResponse<Response> 두_역의_경로_조회_요청(final Long source,
                                                               final Long target,
                                                               final String type) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}&type={type}", source, target, type)
                .then().log().all().extract();
    }

    public static void 경로_조회_검증(ExtractableResponse<Response> response, int distance, int duration, Long... expectedStationId) {
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(expectedStationId);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(distance);
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(duration);
    }
}

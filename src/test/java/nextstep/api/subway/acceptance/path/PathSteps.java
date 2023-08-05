package nextstep.api.subway.acceptance.path;

import static nextstep.utils.AcceptanceHelper.statusCodeShouldBe;

import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import nextstep.api.subway.applicaion.path.dto.PathResponse;

public class PathSteps {

    public static final String BASE_URL = "/paths";

    public static ValidatableResponse 최단경로조회_요청(final Long sourceId, final Long targetId,
                                                final RequestSpecification restAssured) {
        return restAssured
                .queryParam("source", sourceId)
                .queryParam("target", targetId)
                .when().get(BASE_URL)
                .then();
    }

    public static ValidatableResponse 최단경로조회_요청(final Long sourceId, final Long targetId) {
        return 최단경로조회_요청(sourceId, targetId, RestAssured.given());
    }

    public static PathResponse 최단경로를_조회한다(final Long sourceId, final Long targetId) {
        final var response = 최단경로조회_요청(sourceId, targetId);

        statusCodeShouldBe(response, HttpStatus.OK);

        return response.extract().as(PathResponse.class);
    }

    public static void 최단경로_조회에_실패한다(final Long sourceId, final Long targetId) {
        final var response = RestAssured.given()
                .when().get(String.format("%s?source=%d&target=%d", BASE_URL, sourceId, targetId))
                .then();

        statusCodeShouldBe(response, HttpStatus.BAD_REQUEST);
    }
}

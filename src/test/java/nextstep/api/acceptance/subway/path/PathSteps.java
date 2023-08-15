package nextstep.api.acceptance.subway.path;

import static nextstep.api.acceptance.AcceptanceHelper.asExceptionResponse;
import static nextstep.api.acceptance.AcceptanceHelper.asResponse;
import static nextstep.api.acceptance.AcceptanceHelper.statusCodeShouldBe;

import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import nextstep.api.ExceptionResponse;
import nextstep.api.subway.applicaion.path.dto.PathResponse;

public class PathSteps {

    public static final String BASE_URL = "/paths";

    public static ValidatableResponse 최단경로조회_요청(final Long sourceId, final Long targetId, final String type,
                                                final RequestSpecification restAssured) {
        return restAssured
                .queryParam("source", sourceId)
                .queryParam("target", targetId)
                .queryParam("type", type)
                .when().get(BASE_URL)
                .then();
    }

    public static ValidatableResponse 최단경로조회_요청(final Long sourceId, final Long targetId, final String type) {
        return 최단경로조회_요청(sourceId, targetId, type, RestAssured.given());
    }

    public static PathResponse 최단경로조회_성공(final Long sourceId, final Long targetId, final String type) {
        final var response = 최단경로조회_요청(sourceId, targetId, type);
        statusCodeShouldBe(response, HttpStatus.OK);
        return asResponse(response, PathResponse.class);
    }

    public static ExceptionResponse 최단경로조회_실패(final Long sourceId, final Long targetId, final String type,
                                              final HttpStatus httpStatus) {
        final var response = 최단경로조회_요청(sourceId, targetId, type);
        statusCodeShouldBe(response, httpStatus);
        return asExceptionResponse(response);
    }
}

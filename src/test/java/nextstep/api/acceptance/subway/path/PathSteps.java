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
import nextstep.api.subway.domain.path.PathSelection;

public class PathSteps {

    public static final String BASE_URL = "/paths";

    public static ValidatableResponse 최단경로조회_요청(final Long sourceId, final Long targetId, final PathSelection type,
                                                final RequestSpecification restAssured) {
        return restAssured
                .queryParam("source", sourceId)
                .queryParam("target", targetId)
                .queryParam("type", type.name())
                .when().get(BASE_URL)
                .then();
    }

    public static ValidatableResponse 최단경로조회_요청(final Long sourceId, final Long targetId, final PathSelection type) {
        return 최단경로조회_요청(sourceId, targetId, type, RestAssured.given());
    }

    public static PathResponse 최단경로조회_성공(final Long sourceId, final Long targetId, final PathSelection type) {
        final var response = 최단경로조회_요청(sourceId, targetId, type);
        statusCodeShouldBe(response, HttpStatus.OK);
        return asResponse(response, PathResponse.class);
    }

    public static ExceptionResponse 최단경로조회_실패(final Long sourceId, final Long targetId, final PathSelection type,
                                              final HttpStatus httpStatus) {
        final var response = 최단경로조회_요청(sourceId, targetId, type);
        statusCodeShouldBe(response, httpStatus);
        return asExceptionResponse(response);
    }

    public static ValidatableResponse 최단경로조회_요청(final String token, final Long sourceId, final Long targetId,
                                                final PathSelection type,
                                                final RequestSpecification restAssured) {
        return restAssured
                .header("Authorization", "Bearer " + token)
                .queryParam("source", sourceId)
                .queryParam("target", targetId)
                .queryParam("type", type.name())
                .when().get(BASE_URL)
                .then();
    }

    public static ValidatableResponse 최단경로조회_요청(final String token, final Long sourceId, final Long targetId,
                                                final PathSelection type) {
        return 최단경로조회_요청(token, sourceId, targetId, type, RestAssured.given());
    }

    public static PathResponse 최단경로조회_성공(final String token, final Long sourceId, final Long targetId,
                                         final PathSelection type) {
        final var response = 최단경로조회_요청(token, sourceId, targetId, type);
        statusCodeShouldBe(response, HttpStatus.OK);
        return asResponse(response, PathResponse.class);
    }
}

package nextstep.api.acceptance.subway.line;

import static nextstep.api.acceptance.AcceptanceHelper.asExceptionResponse;
import static nextstep.api.acceptance.AcceptanceHelper.asResponse;
import static nextstep.api.acceptance.AcceptanceHelper.statusCodeShouldBe;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import nextstep.api.ExceptionResponse;
import nextstep.api.subway.applicaion.line.dto.request.SectionRequest;
import nextstep.api.subway.applicaion.line.dto.response.LineResponse;

public class SectionSteps {

    public static final String BASE_URL = "/lines";

    public static ValidatableResponse 지하철구간_등록_요청(
            final Long lineId, final SectionRequest request, final RequestSpecification restAssured) {
        return restAssured
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post(BASE_URL + "/" + lineId + "/sections")
                .then();
    }

    public static ValidatableResponse 지하철구간_등록_요청(final Long lineId, final SectionRequest request) {
        return 지하철구간_등록_요청(lineId, request, RestAssured.given());
    }

    public static ValidatableResponse 지하철구간_등록_요청(final Long lineId, final Long upStationId, final Long downStationId,
                                                  final int distance, final int duration) {
        return 지하철구간_등록_요청(lineId, new SectionRequest(upStationId, downStationId, distance, duration), RestAssured.given());
    }

    public static LineResponse 지하철구간_등록_성공(final Long lineId, final SectionRequest request) {
        final var response = 지하철구간_등록_요청(lineId, request);
        statusCodeShouldBe(response, HttpStatus.OK);
        return asResponse(response, LineResponse.class);
    }

    public static ExceptionResponse 지하철구간_등록_실패(final Long lineId, final SectionRequest request) {
        final var response = 지하철구간_등록_요청(lineId, request);
        statusCodeShouldBe(response, HttpStatus.BAD_REQUEST);
        return asExceptionResponse(response);
    }

    public static ValidatableResponse 지하철구간_제거_요청(final Long lineId, final Long stationId,
                                                  final RequestSpecification restAssured) {
        return restAssured
                .param("stationId", stationId)
                .when().delete(BASE_URL + "/" + lineId + "/sections")
                .then();
    }

    public static ValidatableResponse 지하철구간_제거_요청(final Long lineId, final Long stationId) {
        return 지하철구간_제거_요청(lineId, stationId, RestAssured.given());
    }

    public static void 지하철구간_제거_성공(final Long lineId, final Long stationId) {
        final var response = 지하철구간_제거_요청(lineId, stationId);
        statusCodeShouldBe(response, HttpStatus.NO_CONTENT);
    }

    public static ExceptionResponse 지하철구간_제거_실패(final Long lineId, final Long stationId) {
        final var response = 지하철구간_제거_요청(lineId, stationId);
        statusCodeShouldBe(response, HttpStatus.BAD_REQUEST);
        return asExceptionResponse(response);
    }
}

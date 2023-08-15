package nextstep.api.acceptance.subway.line;

import static nextstep.api.acceptance.AcceptanceHelper.asExceptionResponse;
import static nextstep.api.acceptance.AcceptanceHelper.asResponse;
import static nextstep.api.acceptance.AcceptanceHelper.asResponses;
import static nextstep.api.acceptance.AcceptanceHelper.statusCodeShouldBe;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import nextstep.api.ExceptionResponse;
import nextstep.api.subway.applicaion.line.dto.request.LineCreateRequest;
import nextstep.api.subway.applicaion.line.dto.request.LineUpdateRequest;
import nextstep.api.subway.applicaion.line.dto.response.LineResponse;

public class LineSteps {

    public static final String BASE_URL = "/lines";

    public static ValidatableResponse 지하철노선_생성_요청(
            final LineCreateRequest request, final RequestSpecification restAssured
    ) {
        return restAssured
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post(BASE_URL)
                .then();
    }

    public static ValidatableResponse 지하철노선_생성_요청(final LineCreateRequest request) {
        return 지하철노선_생성_요청(request, RestAssured.given());
    }

    public static LineResponse 지하철노선_생성_성공(final LineCreateRequest request) {
        final var response = 지하철노선_생성_요청(request);
        statusCodeShouldBe(response, HttpStatus.CREATED);
        return asResponse(response, LineResponse.class);
    }

    public static void 지하철노선_생성_성공(final List<LineCreateRequest> requests) {
        requests.forEach(LineSteps::지하철노선_생성_성공);
    }

    public static LineResponse 지하철노선_생성_성공(final String name, final String color,
                                           final Long upStationId, final Long downStationId,
                                           final int distance, final int duration, final int fare) {
        final var request = new LineCreateRequest(name, color, upStationId, downStationId, distance, duration, fare);
        return 지하철노선_생성_성공(request);
    }

    public static LineResponse 지하철노선_생성_성공(final Long upStationId, final Long downStationId,
                                           final int distance, final int duration, final int fare) {
        final var request = new LineCreateRequest("신분당선", "bg-red-600", upStationId, downStationId, distance, duration, fare);
        return 지하철노선_생성_성공(request);
    }

    public static ValidatableResponse 지하철노선_전체조회_요청(final RequestSpecification restAssured) {
        return restAssured
                .when().get(BASE_URL)
                .then();
    }

    public static ValidatableResponse 지하철노선_전체조회_요청() {
        return 지하철노선_전체조회_요청(RestAssured.given());
    }

    public static List<LineResponse> 지하철노선_전체조회_성공() {
        final var response = 지하철노선_전체조회_요청();
        statusCodeShouldBe(response, HttpStatus.OK);
        return asResponses(response, LineResponse.class);
    }

    public static ValidatableResponse 지하철노선_단일조회_요청(final Long id, final RequestSpecification restAssured) {
        return restAssured
                .pathParams("id", id)
                .when().get(BASE_URL + "/{id}")
                .then();
    }

    public static ValidatableResponse 지하철노선_단일조회_요청(final Long id) {
        return 지하철노선_단일조회_요청(id, RestAssured.given());
    }

    public static LineResponse 지하철노선을_조회한다(final Long id) {
        final var response = 지하철노선_단일조회_요청(id);
        statusCodeShouldBe(response, HttpStatus.OK);
        return asResponse(response, LineResponse.class);
    }

    public static ValidatableResponse 지하철노선_수정_요청(final Long id, final LineUpdateRequest request,
                                                  final RequestSpecification restAssured) {
        return restAssured
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParams("id", id)
                .body(request)
                .when().put(BASE_URL + "/{id}")
                .then();
    }

    public static ValidatableResponse 지하철노선_수정_요청(final Long id, final LineUpdateRequest request) {
        return 지하철노선_수정_요청(id, request, RestAssured.given());
    }

    public static void 지하철노선_수정_성공(final Long id, final LineUpdateRequest request) {
        final var response = 지하철노선_수정_요청(id, request);
        statusCodeShouldBe(response, HttpStatus.OK);
    }

    public static ExceptionResponse 지하철노선_수정_실패(final Long id, final LineUpdateRequest request) {
        final var response = 지하철노선_수정_요청(id, request);
        statusCodeShouldBe(response, HttpStatus.BAD_REQUEST);
        return asExceptionResponse(response);
    }

    public static ValidatableResponse 지하철노선_제거_요청(final Long id, final RequestSpecification restAssured) {
        return restAssured
                .pathParams("id", id)
                .when().delete(BASE_URL + "/{id}")
                .then();
    }

    public static ValidatableResponse 지하철노선_제거_요청(final Long id) {
        return 지하철노선_제거_요청(id, RestAssured.given());
    }

    public static void 지하철노선_제거_성공(final Long id) {
        final var response = 지하철노선_제거_요청(id);
        statusCodeShouldBe(response, HttpStatus.NO_CONTENT);
    }
}

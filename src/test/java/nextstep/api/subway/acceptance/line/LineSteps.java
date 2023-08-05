package nextstep.api.subway.acceptance.line;

import static nextstep.utils.AcceptanceHelper.statusCodeShouldBe;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import nextstep.api.subway.applicaion.line.dto.request.LineCreateRequest;
import nextstep.api.subway.applicaion.line.dto.request.LineUpdateRequest;
import nextstep.api.subway.applicaion.line.dto.response.LineResponse;

public class LineSteps {

    public static final String BASE_URL = "/lines";

    public static void 지하철노선을_생성한다(final List<LineCreateRequest> requests) {
        requests.forEach(LineSteps::지하철노선을_생성한다);
    }

    public static ValidatableResponse 지하철노선_생성_요청(final String name, final String color, final Long upStationId,
                                                  final Long downStationId, final int distance,
                                                  final RequestSpecification restAssured) {
        final var request = new LineCreateRequest(name, color, upStationId, downStationId, distance);
        return 지하철노선_생성_요청(request, restAssured);
    }

    public static ValidatableResponse 지하철노선_생성_요청(final LineCreateRequest request,
                                                  final RequestSpecification restAssured) {
        return restAssured
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post(BASE_URL)
                .then();
    }


    public static LineResponse 지하철노선을_생성한다(final LineCreateRequest request) {
        final var response = 지하철노선_생성_요청(request, RestAssured.given());

        statusCodeShouldBe(response, HttpStatus.CREATED);

        return response.extract()
                .jsonPath()
                .getObject("", LineResponse.class);
    }

    public static LineResponse 지하철노선을_생성한다(final String name, final String color,
                                           final Long upStationId, final Long downStationId, final int distance) {
        final var request = new LineCreateRequest(name, color, upStationId, downStationId, distance);
        return 지하철노선을_생성한다(request);
    }

    public static LineResponse 지하철노선을_생성한다(final Long upStationId, final Long downStationId, final int distance) {
        final var request = new LineCreateRequest("신분당선", "bg-red-600", upStationId, downStationId, distance);
        return 지하철노선을_생성한다(request);
    }

    public static ValidatableResponse 지하철노선_전체조회_요청(final RequestSpecification restAssured) {
        return restAssured
                .when().get(BASE_URL)
                .then();
    }

    public static List<LineResponse> 모든_지하철노선을_조회한다() {
        final var response = 지하철노선_전체조회_요청(RestAssured.given());

        statusCodeShouldBe(response, HttpStatus.OK);

        return response.extract()
                .jsonPath()
                .getList("", LineResponse.class);
    }

    public static ValidatableResponse 지하철노선_단일조회_요청(final Long id,final RequestSpecification restAssured ) {
        return restAssured
                .when().get(BASE_URL + "/" + id)
                .then();
    }

    public static LineResponse 지하철노선을_조회한다(final Long id) {
        final var response = 지하철노선_단일조회_요청(id, RestAssured.given());

        statusCodeShouldBe(response, HttpStatus.OK);

        return response.extract()
                .jsonPath()
                .getObject("", LineResponse.class);
    }

    public static ValidatableResponse 지하철노선_수정_요청(final Long id, final LineUpdateRequest request,
                                                  final RequestSpecification restAssured) {
        return restAssured
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().put(BASE_URL + "/" + id)
                .then();
    }

    public static void 지하철노선을_수정한다(final Long id, final LineUpdateRequest request) {
        final var response = 지하철노선_수정_요청(id, request, RestAssured.given());

        statusCodeShouldBe(response, HttpStatus.OK);
    }

    public static void 지하철노선_수정에_실패한다(final Long id, final LineUpdateRequest request) {
        final var response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().put(BASE_URL + "/" + id)
                .then();

        statusCodeShouldBe(response, HttpStatus.BAD_REQUEST);
    }

    public static ValidatableResponse 지하철노선_제거_요청(final Long id, final RequestSpecification restAssured) {
        return restAssured
                .when().delete(BASE_URL + "/" + id)
                .then();
    }

    public static void 지하철노선을_제거한다(final Long id) {
        final var response = 지하철노선_제거_요청(id, RestAssured.given());

        statusCodeShouldBe(response, HttpStatus.NO_CONTENT);
    }
}

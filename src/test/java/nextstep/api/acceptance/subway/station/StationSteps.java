package nextstep.api.acceptance.subway.station;

import static nextstep.api.acceptance.AcceptanceHelper.asResponse;
import static nextstep.api.acceptance.AcceptanceHelper.asResponses;
import static nextstep.api.acceptance.AcceptanceHelper.statusCodeShouldBe;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import nextstep.api.subway.applicaion.station.dto.StationRequest;
import nextstep.api.subway.applicaion.station.dto.StationResponse;

public class StationSteps {
    public static final String BASE_URL = "/stations";

    public static ValidatableResponse 지하철역_생성_요청(final String name, final RequestSpecification restAssured) {
        return restAssured
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new StationRequest(name))
                .when().post(BASE_URL)
                .then();
    }

    public static ValidatableResponse 지하철역_생성_요청(final String name) {
        return 지하철역_생성_요청(name, RestAssured.given());
    }

    public static StationResponse 지하철역_생성_성공(final String name) {
        final var response = 지하철역_생성_요청(name, RestAssured.given());
        statusCodeShouldBe(response, HttpStatus.CREATED);
        return asResponse(response, StationResponse.class);
    }

    public static void 지하철역_생성_성공(final List<String> names) {
        names.forEach(StationSteps::지하철역_생성_성공);
    }

    public static ValidatableResponse 지하철역_전체조회_요청(final RequestSpecification restAssured) {
        return restAssured
                .when().get(BASE_URL)
                .then();
    }

    public static ValidatableResponse 지하철역_전체조회_요청() {
        return 지하철역_전체조회_요청(RestAssured.given());
    }

    public static List<StationResponse> 지하철역_전체조회_성공() {
        final var response = 지하철역_전체조회_요청();
        statusCodeShouldBe(response, HttpStatus.OK);
        return asResponses(response, StationResponse.class);
    }

    public static List<StationResponse> 지하철역_단일조회_성공(final String name) {
        return 지하철역_전체조회_성공().stream()
                .filter(it -> it.getName().equals(name))
                .collect(Collectors.toUnmodifiableList());
    }

    public static ValidatableResponse 지하철역_제거_요청(final Long stationId, final RequestSpecification restAssured) {
        return restAssured
                .pathParams("id", stationId)
                .when().delete(BASE_URL + "/{id}")
                .then();
    }

    public static ValidatableResponse 지하철역_제거_요청(final Long stationId) {
        return 지하철역_제거_요청(stationId, RestAssured.given());
    }

    public static void 지하철역_제거_성공(final Long stationId) {
        final var response = 지하철역_제거_요청(stationId);
        statusCodeShouldBe(response, HttpStatus.NO_CONTENT);
    }
}

package nextstep.api.subway.acceptance.station;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import nextstep.api.subway.applicaion.station.dto.StationRequest;
import nextstep.api.subway.applicaion.station.dto.StationResponse;
import nextstep.utils.AcceptanceHelper;

public class StationSteps {
    public static final String BASE_URL = "/stations";

    public static ValidatableResponse 지하철역_생성_요청(final String name, final RequestSpecification restAssured) {
        return restAssured
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new StationRequest(name))
                .when().post(BASE_URL)
                .then();
    }

    public static StationResponse 지하철역을_생성한다(final String name) {
        final var response = 지하철역_생성_요청(name, RestAssured.given());

        AcceptanceHelper.statusCodeShouldBe(response, HttpStatus.CREATED);

        return response.extract()
                .jsonPath()
                .getObject("", StationResponse.class);
    }

    public static void 지하철역을_생성한다(final List<String> names) {
        names.forEach(StationSteps::지하철역을_생성한다);
    }

    public static ValidatableResponse 지하철역_전체조회_요청(final RequestSpecification restAssured) {
        return restAssured
                .when().get(BASE_URL)
                .then();
    }

    public static List<StationResponse> 모든_지하철역을_조회한다() {
        final var response = 지하철역_전체조회_요청(RestAssured.given());

        AcceptanceHelper.statusCodeShouldBe(response, HttpStatus.OK);

        return response.extract()
                .jsonPath()
                .getList("", StationResponse.class);
    }

    public static List<StationResponse> 지하철역을_조회한다(final String name) {
        return 모든_지하철역을_조회한다().stream()
                .filter(it -> it.getName().equals(name))
                .collect(Collectors.toUnmodifiableList());
    }

    public static ValidatableResponse 지하철역_제거_요청(final Long stationId, final RequestSpecification restAssured) {
        return restAssured
                .when().delete(BASE_URL + "/" + stationId)
                .then();
    }

    public static void 지하철역을_제거한다(final Long stationId) {
        final var response = 지하철역_제거_요청(stationId, RestAssured.given());

        AcceptanceHelper.statusCodeShouldBe(response, HttpStatus.NO_CONTENT);
    }
}

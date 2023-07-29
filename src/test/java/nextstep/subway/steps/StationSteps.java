package nextstep.subway.steps;

import io.restassured.response.ValidatableResponse;
import nextstep.utils.AcceptanceTestUtils;

import java.util.HashMap;
import java.util.Map;

public class StationSteps {

    public static final String STATIONS_RESOURCE_URL = "/stations";

    public static ValidatableResponse createStation(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return AcceptanceTestUtils.createResource(STATIONS_RESOURCE_URL, params);
    }
}

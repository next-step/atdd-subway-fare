package nextstep.subway.steps;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;
import nextstep.utils.AcceptanceTestUtils;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.steps.StationSteps.createStation;
import static org.assertj.core.api.Assertions.assertThat;

public class LineSteps {

    public static final String LINES_RESOURCE_URL = "/lines";
    public static final String UP_STATION_ID_JSON_PATH = "stations[0].id";
    public static final String DOWN_STATION_ID_JSON_PATH = "stations[1].id";

    public static ValidatableResponse createLines(String lineName, String color, Long upStationId, Long downStationId, long distance, int duration) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", lineName);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        params.put("duration", duration);

        return AcceptanceTestUtils.createResource(LINES_RESOURCE_URL, params);
    }

    public static ValidatableResponse createLinesWithStations(String lineName, String color, String upStationName, String downStationName, long distance, int duration) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", lineName);
        params.put("color", color);
        params.put("upStationId", AcceptanceTestUtils.getId(createStation(upStationName)));
        params.put("downStationId", AcceptanceTestUtils.getId(createStation(downStationName)));
        params.put("distance", distance);
        params.put("duration", duration);

        return AcceptanceTestUtils.createResource(LINES_RESOURCE_URL, params);
    }

    public static void verifyFoundLine(ValidatableResponse foundLineResponse, String lineName, String color, long distance, int duration, String... names) {
        JsonPath jsonPath = foundLineResponse.extract().jsonPath();

        assertThat(jsonPath.getString("name")).isEqualTo(lineName);
        assertThat(jsonPath.getString("color")).isEqualTo(color);
        assertThat(jsonPath.getLong("distance")).isEqualTo(distance);
        assertThat(jsonPath.getLong("duration")).isEqualTo(duration);
        assertThat(jsonPath.getList("stations.name", String.class)).containsExactly(names);
    }

    public static void verifyFoundLineWithPath(String path, ValidatableResponse foundLineResponse, String lineName, String color, long distance, int duration, String... names) {
        JsonPath jsonPath = foundLineResponse.extract().jsonPath();

        assertThat(jsonPath.getString(path + ".name")).isEqualTo(lineName);
        assertThat(jsonPath.getString(path + ".color")).isEqualTo(color);
        assertThat(jsonPath.getLong(path + ".distance")).isEqualTo(distance);
        assertThat(jsonPath.getInt(path + ".duration")).isEqualTo(duration);
        assertThat(jsonPath.getList(path + ".stations.name", String.class)).containsExactly(names);
    }


    public static ValidatableResponse modifyLine(String lineName, String color, String url) {
        Map<String, String> params = new HashMap<>();
        params.put("name", lineName);
        params.put("color", color);

        return AcceptanceTestUtils.modifyResource(url, params);
    }
}

package nextstep.subway.steps;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;
import nextstep.subway.domain.enums.PathType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.utils.AcceptanceTestUtils.getResource;
import static org.assertj.core.api.Assertions.assertThat;

public class PathSteps {

    public static final String PATH_RESOURCE_URL = "/paths";

    public static ValidatableResponse getPath(Long sourceStationId, Long targetStationId, PathType type) {
        Map<String, Object> params = new HashMap<>();
        params.put("source", sourceStationId);
        params.put("target", targetStationId);
        params.put("type", type);

        return getResource(PATH_RESOURCE_URL, params);
    }

    public static void verifyFoundPath(ValidatableResponse foundPathResponse, long distance, int duration, String... stationNames) {
        JsonPath jsonPath = foundPathResponse.extract().jsonPath();
        assertThat(jsonPath.getList("stationResponses.name", String.class)).containsExactly(stationNames);
        assertThat(jsonPath.getLong("distance")).isEqualTo(distance);
        assertThat(jsonPath.getLong("duration")).isEqualTo(duration);
    }
}

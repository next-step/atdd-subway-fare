package nextstep.subway.steps;

import io.restassured.response.ValidatableResponse;
import nextstep.subway.controller.resonse.LineResponse;
import nextstep.subway.controller.resonse.StationResponse;
import org.junit.jupiter.api.Assertions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.steps.LineSteps.LINES_RESOURCE_URL;
import static nextstep.utils.AcceptanceTestUtils.createResource;
import static org.assertj.core.api.Assertions.assertThat;

public class SectionSteps {

    public static final String SECTION_RESOURCE_URL = "/sections";


    public static ValidatableResponse createSection(Long lineId, long upStationId, long downStationId, long distance) {
        Map<String, Object> params = new HashMap<>();
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        return createResource(String.format("%s/%d%s", LINES_RESOURCE_URL, lineId, SECTION_RESOURCE_URL), params);
    }

    public static void verifyLineResponse(LineResponse response, String name, String color, long distance) {
        Assertions.assertEquals(name, response.getName());
        Assertions.assertEquals(color, response.getColor());
        Assertions.assertEquals(distance, response.getDistance());
    }

    public static void verifyLineResponseStationNames(LineResponse response, String... stationNames) {
        List<StationResponse> stations = response.getStations();
        assertThat(stations).hasSize(stationNames.length)
                .map(StationResponse::getName)
                .containsExactly(stationNames);
    }
}

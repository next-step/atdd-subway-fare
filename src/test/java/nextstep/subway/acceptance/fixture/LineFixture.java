package nextstep.subway.acceptance.fixture;

import java.util.HashMap;
import java.util.Map;

public class LineFixture {
    public static Map<String, Object> createLineParams(String name, String color, Long upStationId, Long downStationId, Long distance, Long duration, Long additionalFare) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        params.put("duration", duration);
        params.put("additionalFare", additionalFare);
        return params;
    }

    public static Map<String, Object> updateLineParams(String name, String color) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        return params;
    }
}

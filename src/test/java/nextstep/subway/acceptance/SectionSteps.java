package nextstep.subway.acceptance;

import java.util.HashMap;
import java.util.Map;

public class SectionSteps {

    public static Map<String, String> createSectionCreateParams(
            final Long upStationId,
            final Long downStationId,
            final int distance,
            final int duration
    ) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance + "");
        params.put("duration", duration + "");
        return params;
    }
}

package nextstep.subway.acceptance;

import java.util.HashMap;
import java.util.Map;

public class SectionSteps {

    public static Map<String, String> 구간_생성_요청값_생성(Long upStationId, Long downStationId, int distance, int duration) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance + "");
        params.put("duration", duration + "");
        return params;
    }
}

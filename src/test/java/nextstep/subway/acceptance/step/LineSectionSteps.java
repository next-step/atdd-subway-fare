package nextstep.subway.acceptance.step;

import java.util.HashMap;
import java.util.Map;

public class LineSectionSteps {

    public static Map<String, String> 구간_추가_요청_생성(Long upStationId, Long downStationId) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", String.valueOf(upStationId));
        params.put("downStationId", String.valueOf(downStationId));
        params.put("distance", String.valueOf(6));
        params.put("duration", String.valueOf(10));
        return params;
    }

}

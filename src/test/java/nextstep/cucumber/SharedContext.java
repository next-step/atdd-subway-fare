package nextstep.cucumber;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SharedContext {
    private final Map<String, Long> stationIds = new HashMap<>();
    private final Map<String, Long> lineIds = new HashMap<>();
    private final Map<String, Long> memberIds = new HashMap<>();

    public void addStationId(String stationName, Long id) {
        stationIds.put(stationName, id);
    }

    public Long getStationId(String stationName) {
        return stationIds.get(stationName);
    }

    public void addLineId(String lineName, Long id) {
        lineIds.put(lineName, id);
    }

    public Long getLineId(String lineName) {
        return lineIds.get(lineName);
    }

    public void addMemberId(String email, Long id) {
        memberIds.put(email, id);
    }
}

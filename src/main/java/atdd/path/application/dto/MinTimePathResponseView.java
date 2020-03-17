package atdd.path.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class MinTimePathResponseView {
    private long startStationId;
    private long endStationId;
    private List<StationSimpleResponseView> stations;
    private List<LineSimpleResponseView> lines;
    private int distance;
    private LocalDateTime departAt;
    private LocalDateTime arriveBy;

    public MinTimePathResponseView() {
    }

    @Builder
    private MinTimePathResponseView(long startStationId,
                                    long endStationId,
                                    List<StationSimpleResponseView> stations,
                                    List<LineSimpleResponseView> lines,
                                    int distance,
                                    LocalDateTime departAt,
                                    LocalDateTime arriveBy) {
        this.startStationId = startStationId;
        this.endStationId = endStationId;
        this.stations = stations;
        this.lines = lines;
        this.distance = distance;
        this.departAt = departAt;
        this.arriveBy = arriveBy;
    }
}

package nextstep.subway.controller.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PathResponse {
    private List<StationResponse> stations;
    private long distance;
    private long duration;
    private long fare;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, long distance, long duration) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
    }

}

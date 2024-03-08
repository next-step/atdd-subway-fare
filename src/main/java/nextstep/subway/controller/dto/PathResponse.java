package nextstep.subway.controller.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PathResponse {
    private List<StationResponse> stations;
    private Long distance;
    private Long duration;

    @Builder
    public PathResponse(List<StationResponse> stations, Long distance, Long duration) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
    }
}

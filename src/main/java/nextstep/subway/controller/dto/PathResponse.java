package nextstep.subway.controller.dto;

import lombok.Builder;
import lombok.Getter;
import nextstep.subway.domain.Path;

import java.util.List;

import static nextstep.subway.controller.dto.StationResponse.stationsToStationResponses;

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

    public static PathResponse from(Path path) {
        return PathResponse.builder()
                .stations(stationsToStationResponses(path.getPath()))
                .distance(path.getDistance())
                .duration(path.getDuration())
                .build();
    }
}

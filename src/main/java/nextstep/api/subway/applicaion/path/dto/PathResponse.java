package nextstep.api.subway.applicaion.path.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.api.subway.applicaion.station.dto.StationResponse;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PathResponse {
    private List<StationResponse> stations;
    private long distance;
    private long duration;

    public PathResponse(final List<StationResponse> stations, final long distance, final long duration) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
    }
}

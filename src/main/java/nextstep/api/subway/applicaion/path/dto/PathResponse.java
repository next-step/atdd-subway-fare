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
    private long total;

    public PathResponse(final List<StationResponse> stations, final long total) {
        this.stations = stations;
        this.total = total;
    }
}

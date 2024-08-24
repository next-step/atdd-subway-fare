package nextstep.subway.application;

import lombok.Builder;
import lombok.Getter;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class PathResponse {
    private List<StationResponse> stations;
    private PathType pathType;
    private int amount;
    private int fare;

    public static PathResponse of(Path path, PathType pathType) {
        List<Station> stations = path.getStations();
        List<StationResponse> stationResponses = stations.stream().map(StationResponse::of).collect(Collectors.toList());
        return PathResponse.builder()
                .stations(stationResponses)
                .pathType(pathType)
                .amount(path.getTotalWeight())
                .fare(path.getFare())
                .build();
    }
}

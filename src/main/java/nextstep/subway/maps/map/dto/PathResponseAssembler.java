package nextstep.subway.maps.map.dto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import nextstep.subway.maps.map.domain.SubwayPath;
import nextstep.subway.maps.station.domain.Station;
import nextstep.subway.maps.station.dto.StationResponse;

public class PathResponseAssembler {

    public static FarePathResponse assemble(SubwayPath subwayPath, Map<Long, Station> stations, int fare) {
        List<StationResponse> stationResponses = subwayPath.extractStationId().stream()
            .map(it -> StationResponse.of(stations.get(it)))
            .collect(Collectors.toList());
        int distance = subwayPath.calculateDistance();
        return new FarePathResponse(stationResponses, subwayPath.calculateDuration(), distance, fare);
    }
}

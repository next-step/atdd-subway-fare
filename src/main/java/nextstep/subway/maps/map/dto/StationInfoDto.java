package nextstep.subway.maps.map.dto;

import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.map.application.FareCalculator;
import nextstep.subway.maps.map.domain.LineStationEdge;
import nextstep.subway.maps.map.domain.SubwayPath;
import nextstep.subway.maps.station.domain.Station;
import nextstep.subway.maps.station.dto.StationResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StationInfoDto {

    private SubwayPath subwayPath;
    private Map<Long, Station> stations;

    public StationInfoDto(SubwayPath subwayPath, Map<Long, Station> stations) {
        this.subwayPath = subwayPath;
        this.stations = stations;
    }

    public List<StationResponse> getStationResponses() {
        return subwayPath.extractStationId().stream()
                .map(it -> StationResponse.of(stations.get(it)))
                .collect(Collectors.toList());
    }

    public int getDistance() {
        return subwayPath.calculateDistance();
    }

    public int getDuration() {
        return subwayPath.calculateDuration();
    }

    public int getMaxExtraFare() {
        return subwayPath.getLineStationEdges().stream()
                .map(LineStationEdge::getLine)
                .map(Line::getExtraFare)
                .max(Integer::compareTo)
                .orElseThrow(IllegalArgumentException::new);
    }

}

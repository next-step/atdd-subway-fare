package nextstep.subway.maps.map.domain.path;

import java.time.LocalDateTime;
import java.time.LocalTime;

import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.line.domain.LineStation;
import nextstep.subway.maps.map.domain.LineStationEdge;
import nextstep.subway.maps.map.domain.SubwayPath;

public class TimePath {

    private final SubwayPath path;

    public TimePath(SubwayPath path) {
        this.path = path;
    }

    public SubwayPath getPath() {
        return path;
    }

    public LocalDateTime getArrivalTime(LocalDateTime departTime) {
        LocalTime stationArrivalTime = departTime.toLocalTime();
        for (LineStationEdge lineStationEdge: path.getLineStationEdges()) {
            LineStation lineStation = lineStationEdge.getLineStation();
            Line line = lineStationEdge.getLine();
            Long pendingStationId = lineStation.getPreStationId();
            LocalTime nextTime = line.calculateNextDepartureTime(pendingStationId, stationArrivalTime);
            stationArrivalTime = nextTime.plusMinutes(lineStation.getDuration());
        }
        return stationArrivalTime.atDate(departTime.toLocalDate());
    }
}

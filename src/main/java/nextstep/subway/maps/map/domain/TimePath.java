package nextstep.subway.maps.map.domain;

import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.line.domain.LineStation;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimePath {

    private final SubwayPath path;

    public TimePath(SubwayPath path) {
        this.path = path;
    }

    public SubwayPath getPath() {
        return path;
    }

    public LocalDateTime getArrivalTime(LocalDateTime departTime) {
        LocalTime StationArrivedTime = departTime.toLocalTime();
        for (LineStationEdge lineStationEdge : path.getLineStationEdges()) {
            LineStation lineStation = lineStationEdge.getLineStation();
            Line line = lineStationEdge.getLine();

            Long waitingStationId = lineStation.getPreStationId();
            LocalTime nextTime = line.calculateNextDepartureTime(waitingStationId, StationArrivedTime);
            StationArrivedTime = nextTime.plusMinutes(lineStation.getDuration());
        }

        return StationArrivedTime.atDate(departTime.toLocalDate());
    }
}

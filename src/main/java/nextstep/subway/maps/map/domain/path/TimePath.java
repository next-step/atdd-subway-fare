package nextstep.subway.maps.map.domain.path;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

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
        LocalTime lastStationArrivalTime = departTime.toLocalTime();
        for (LineStationEdge lineStationEdge: path.getLineStationEdges()) {
            LineStation lineStation = lineStationEdge.getLineStation();
            Line line = lineStationEdge.getLine();
            Long pendingStationId = lineStation.getPreStationId();
            LocalTime nextTime = line.calculateNextTime(pendingStationId, lastStationArrivalTime);
            lastStationArrivalTime = nextTime.plusMinutes(lineStation.getDuration());
        }
        return lastStationArrivalTime.atDate(departTime.toLocalDate());
    }
}

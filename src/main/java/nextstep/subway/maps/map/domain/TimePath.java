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
        LocalTime stationArrivedTime = departTime.toLocalTime();
        for (LineStationEdge lineStationEdge : path.getLineStationEdges()) {
            LocalTime nextTime = lineStationEdge.calculateNextDepartureTime(stationArrivedTime);
            stationArrivedTime = lineStationEdge.calculateArrivedTime(nextTime);
        }

        return stationArrivedTime.atDate(departTime.toLocalDate());
    }
}

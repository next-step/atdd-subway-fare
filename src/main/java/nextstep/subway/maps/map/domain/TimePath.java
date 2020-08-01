package nextstep.subway.maps.map.domain;

import nextstep.subway.maps.line.domain.Line;

import java.time.Duration;
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
        // 추가 구현 필요
        long waitingMinutes = 0;
        for (LineStationEdge lineStationEdge : path.getLineStationEdges()) {
            Line line = lineStationEdge.getLine();
            Long source = (Long) lineStationEdge.getSource();
            LocalTime departLocalTime = departTime.toLocalTime();

            LocalTime nextTime = line.calculateNextTime(source, departLocalTime);
            waitingMinutes += Duration.between(departLocalTime, nextTime).toMinutes();
        }
        return departTime.plusMinutes(path.calculateDuration()).plusMinutes(waitingMinutes);
    }
}

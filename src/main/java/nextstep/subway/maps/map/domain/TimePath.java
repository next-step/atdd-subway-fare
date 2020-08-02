package nextstep.subway.maps.map.domain;

import nextstep.subway.maps.line.domain.Line;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class TimePath {

    private final SubwayPath path;

    public TimePath(SubwayPath path) {
        this.path = path;
    }

    public SubwayPath getPath() {
        return path;
    }

    public LocalDateTime getArrivalTime(LocalDateTime departTime) {
        Long lastLineId = null;
        LocalTime lastWaitingStartTime = departTime.toLocalTime();
        for (LineStationEdge lineStationEdge : path.getLineStationEdges()) {
            lastWaitingStartTime = lastWaitingStartTime.plusMinutes(lineStationEdge.getLineStation().getDuration());
            if (Objects.equals(lastLineId, lineStationEdge.getLine().getId())) {
                continue;
            }
            Line line = lineStationEdge.getLine();
            Long waitingStationId = lineStationEdge.getLineStation().getStationId();


            LocalTime nextTime = line.calculateNextTime(waitingStationId, lastWaitingStartTime);
            lastWaitingStartTime = lastWaitingStartTime.plusMinutes(Duration.between(lastWaitingStartTime, nextTime).toMinutes());
            lastLineId = line.getId();
        }
        return lastWaitingStartTime.atDate(departTime.toLocalDate());
    }
}

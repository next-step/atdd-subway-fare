package nextstep.subway.maps.map.application.path;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

import nextstep.subway.maps.line.domain.Line;
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
        Long lastLineId = null;
        LocalTime lastPendingStartTime = departTime.toLocalTime();
        for (LineStationEdge lineStationEdge : path.getLineStationEdges()) {
            lastPendingStartTime = lastPendingStartTime.plusMinutes(lineStationEdge.getLineStation().getDuration());
            if (Objects.equals(lastLineId, lineStationEdge.getLine().getId())) {
                continue;
            }
            Line line = lineStationEdge.getLine();
            Long pendingStationId = lineStationEdge.getLineStation().getStationId();
            LocalTime nextTime = line.calculateNextTime(pendingStationId, lastPendingStartTime);
            lastPendingStartTime = lastPendingStartTime.plusMinutes(
                Duration.between(lastPendingStartTime, nextTime).toMinutes()
            );
            lastLineId = line.getId();
        }
        return lastPendingStartTime.atDate(departTime.toLocalDate());
    }
}

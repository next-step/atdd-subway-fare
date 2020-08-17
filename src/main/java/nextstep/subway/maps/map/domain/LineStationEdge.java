package nextstep.subway.maps.map.domain;

import java.time.LocalTime;

import org.jgrapht.graph.DefaultWeightedEdge;

import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.line.domain.LineStation;

public class LineStationEdge extends DefaultWeightedEdge {
    private LineStation lineStation;
    private Line line;

    public LineStationEdge(LineStation lineStation, Line line) {
        this.lineStation = lineStation;
        this.line = line;
    }

    public LineStation getLineStation() {
        return lineStation;
    }

    public Line getLine() {
        return line;
    }

    @Override
    public Object getSource() {
        return this.lineStation.getPreStationId();
    }

    @Override
    protected Object getTarget() {
        return this.lineStation.getStationId();
    }

    public Long extractTargetStationId(Long preStationId) {
        if (lineStation.getStationId().equals(preStationId)) {
            return lineStation.getPreStationId();
        } else if (lineStation.getPreStationId().equals(preStationId)) {
            return lineStation.getStationId();
        } else {
            throw new RuntimeException();
        }
    }

    public LocalTime calculateNextDepartureTime(LocalTime stationArrivalTime) {
        return line.calculateNextDepartureTime(lineStation.getPreStationId(), stationArrivalTime);
    }

    public LocalTime calculateArrivedTime(LocalTime time) {
        return time.plusMinutes(lineStation.getDuration());
    }
}

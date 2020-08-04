package nextstep.subway.maps.map.domain;

import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.line.domain.LineStation;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.time.LocalTime;

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
    protected Object getSource() {
        return this.lineStation.getPreStationId();
    }

    @Override
    protected Object getTarget() {
        return this.lineStation.getStationId();
    }

    public LocalTime calculateNextDepartureTime(LocalTime stationArrivedTime, PathDirection pathDirection) {
        return line.calculateNextDepartureTime(lineStation.getPreStationId(), stationArrivedTime, pathDirection);
    }

    public LocalTime calculateArrivedTime(LocalTime time) {
        return time.plusMinutes(lineStation.getDuration());
    }

}

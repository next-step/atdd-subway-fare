package nextstep.subway.maps.map.domain;

import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.line.domain.LineStation;
import nextstep.subway.maps.line.domain.LineStations;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class SubwayPathSection {

    private final Line line;
    private final List<LineStationEdge> lineStationEdges = new ArrayList<>();

    public SubwayPathSection(Line line) {
        this.line = line;
    }

    public void addLineStationEdge(LineStationEdge lineStationEdge) {
        lineStationEdges.add(lineStationEdge);
    }

    public List<LineStationEdge> getLineStationEdges() {
        return lineStationEdges;
    }

    public Long getFirstStationId() {
        if (this.lineStationEdges.isEmpty()) {
            throw new RuntimeException("sections are empty");
        }
        return this.lineStationEdges.get(0).getLineStation().getPreStationId();
    }

    public Long getLastStationId() {
        int lineStationSize = this.lineStationEdges.size();
        return this.lineStationEdges.get(lineStationSize - 1).getLineStation().getStationId();
    }

    public PathDirection getDirection() {
        return PathDirection.getDirection(this.lineStationEdges);
    }

    public LocalTime getRideTime(LocalTime time) {
        PathDirection direction = getDirection();

        Long firstStationId = this.getFirstStationId();

        if (direction == PathDirection.FORWARD) {
            LocalTime departureTime = line.calculateForwardDepartureTime(time);
            LineStations lineStations = line.getLineStations();
            List<LineStation> orderedLineStations = lineStations.getStationsInOrder();

            int totalDuration = 0;

            for (LineStation lineStation : orderedLineStations) {
                if (lineStation.getPreStationId() == null) {
                    continue;
                }

                totalDuration += lineStation.getDuration();

                if (lineStation.getStationId().equals(firstStationId)) {
                    break;
                }
            }

            return departureTime.plus(totalDuration, ChronoUnit.MINUTES);
        }

        return null;
    }

    public LocalTime getAlightTime(LocalTime time) {
        LocalTime rideTime = this.getRideTime(time);
        int totalDuration = this.getLineStationEdges().stream()
                .map(LineStationEdge::getLineStation)
                .mapToInt(LineStation::getDuration).sum();
        return rideTime.plus(totalDuration, ChronoUnit.MINUTES);
    }
}

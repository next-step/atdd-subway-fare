package nextstep.subway.maps.map.domain;

import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.line.domain.LineStation;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

        LineStationEdge lineStationEdge = this.getLineStationEdges().stream()
                .findFirst().orElseThrow(RuntimeException::new);

        PathDirection direction = getDirection();

        if (direction == PathDirection.FORWARD) {
            return lineStationEdge.getLineStation().getPreStationId();
        }

        return lineStationEdge.getLineStation().getStationId();
    }

    public Long getLastStationId() {
        int lineStationSize = this.lineStationEdges.size();
        PathDirection direction = getDirection();

        if (direction == PathDirection.FORWARD) {
            return this.lineStationEdges.get(lineStationSize - 1).getLineStation().getStationId();
        }

        return this.lineStationEdges.get(lineStationSize - 1).getLineStation().getPreStationId();
    }

    public PathDirection getDirection() {
        return PathDirection.getDirection(this.lineStationEdges);
    }

    public LocalTime getRideTime(LocalTime time) {
        PathDirection direction = getDirection();

        LocalTime departureTime = line.calculateForwardDepartureTime(time);
        Long firstStationId = this.getFirstStationId();

        int totalDuration = 0;

        if (direction == PathDirection.FORWARD) {

            List<LineStation> orderedLineStations = line.getStationInOrder();
            for (LineStation lineStation : orderedLineStations) {
                if (lineStation.getPreStationId() == null) {
                    if (lineStation.getStationId().equals(firstStationId)) {
                        break;
                    }

                    continue;
                }

                totalDuration += lineStation.getDuration();

                if (lineStation.getStationId().equals(firstStationId)) {
                    break;
                }
            }

            return departureTime.plus(totalDuration, ChronoUnit.MINUTES);
        } else {
            List<LineStation> orderedLineStations = line.getStationsInReverseOrder();
            Long lastStationId = getLastStationId();
            for (int index = 0; index < orderedLineStations.size(); index++) {
                LineStation lineStation = orderedLineStations.get(index);
                if (index == 0) {
                    if (lineStation.getStationId().equals(firstStationId)) {
                        break;
                    }
                }

                totalDuration += lineStation.getDuration();

                if (Objects.equals(lastStationId, lineStation.getPreStationId())) {
                    break;
                }
            }

            return departureTime.plus(totalDuration, ChronoUnit.MINUTES);
        }
    }

    public LocalTime getAlightTime(LocalTime time) {
        PathDirection direction = getDirection();

        LocalTime rideTime = this.getRideTime(time);
        int totalDuration = 0;
        if (direction == PathDirection.FORWARD) {
            totalDuration = this.getLineStationEdges().stream()
                    .map(LineStationEdge::getLineStation)
                    .mapToInt(LineStation::getDuration).sum();
        } else {
            LineStation sourceLineStation = this.line.getStationsInReverseOrder().stream()
                    .filter(lineStation -> lineStation.getStationId().equals(this.getFirstStationId()))
                    .findFirst().orElseThrow(RuntimeException::new);
            totalDuration += sourceLineStation.getDuration();

            int size = this.getLineStationEdges().size();
            totalDuration += this.getLineStationEdges().stream().limit(size - 1)
                    .map(LineStationEdge::getLineStation)
                    .mapToInt(LineStation::getDuration).sum();
        }
        return rideTime.plus(totalDuration, ChronoUnit.MINUTES);
    }
}

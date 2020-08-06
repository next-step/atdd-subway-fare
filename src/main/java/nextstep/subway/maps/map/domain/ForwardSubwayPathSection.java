package nextstep.subway.maps.map.domain;

import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.line.domain.LineStation;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ForwardSubwayPathSection extends SubwayPathSection {
    public ForwardSubwayPathSection(Line line) {
        super(line);
    }

    @Override
    public Long getFirstStationId() {
        if (this.getLineStationEdges().isEmpty()) {
            throw new RuntimeException("sections are empty");
        }

        LineStationEdge lineStationEdge = this.getLineStationEdges().stream()
                .findFirst().orElseThrow(RuntimeException::new);

        return lineStationEdge.getLineStation().getPreStationId();

    }

    @Override
    public Long getLastStationId() {
        List<LineStationEdge> lineStationEdges = this.getLineStationEdges();
        int lineStationSize = lineStationEdges.size();

        return lineStationEdges.get(lineStationSize - 1).getLineStation().getStationId();
    }

    @Override
    public LocalTime getRideTime(LocalTime time) {

        LocalTime departureTime = line.calculateForwardDepartureTime(time);
        Long firstStationId = this.getFirstStationId();

        int totalDuration = 0;

        List<LineStation> orderedLineStations = this.line.getStationInOrder();
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

    }

    @Override
    public LocalTime getAlightTime(LocalTime time) {

        LocalTime rideTime = this.getRideTime(time);
        int totalDuration = this.getLineStationEdges().stream()
                .map(LineStationEdge::getLineStation)
                .mapToInt(LineStation::getDuration).sum();


        return rideTime.plus(totalDuration, ChronoUnit.MINUTES);
    }
}

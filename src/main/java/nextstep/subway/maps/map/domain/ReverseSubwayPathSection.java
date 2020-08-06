package nextstep.subway.maps.map.domain;

import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.line.domain.LineStation;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

public class ReverseSubwayPathSection extends SubwayPathSection {
    public ReverseSubwayPathSection(Line line) {
        super(line);
    }

    @Override
    public Long getFirstStationId() {
        LineStationEdge lineStationEdge = this.getLineStationEdges().stream()
                .findFirst().orElseThrow(RuntimeException::new);
        return lineStationEdge.getLineStation().getStationId();
    }

    @Override
    public Long getLastStationId() {
        List<LineStationEdge> lineStationEdges = this.getLineStationEdges();
        int lineStationSize = lineStationEdges.size();

        if (lineStationSize == 0) {
            throw new RuntimeException("edge is empty");
        }

        return lineStationEdges.get(lineStationSize - 1)
                .getLineStation().getPreStationId();
    }

    @Override
    public LocalTime getRideTime(LocalTime time) {

        LocalTime departureTime = line.calculateForwardDepartureTime(time);
        Long firstStationId = this.getFirstStationId();

        int totalDuration = 0;


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

    @Override
    public LocalTime getAlightTime(LocalTime time) {
        LocalTime rideTime = this.getRideTime(time);

        LineStation sourceLineStation = this.line.getStationsInReverseOrder().stream()
                .filter(lineStation -> lineStation.getStationId().equals(this.getFirstStationId()))
                .findFirst().orElseThrow(RuntimeException::new);
        int totalDuration = sourceLineStation.getDuration();

        int size = this.getLineStationEdges().size();
        totalDuration += this.getLineStationEdges().stream().limit(size - 1)
                .map(LineStationEdge::getLineStation)
                .mapToInt(LineStation::getDuration).sum();

        return rideTime.plus(totalDuration, ChronoUnit.MINUTES);
    }
}

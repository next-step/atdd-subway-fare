package nextstep.subway.maps.line.domain;

import nextstep.subway.maps.map.domain.PathDirection;

import javax.persistence.*;
import java.time.Duration;
import java.util.*;

@Embeddable
public class LineStations {
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "line_id", foreignKey = @ForeignKey(name = "fk_line_station_to_line"))
    private List<LineStation> lineStations = new ArrayList<>();

    public List<LineStation> getLineStations() {
        return lineStations;
    }

    public List<LineStation> getStationsInOrder() {
        // 출발지점 찾기
        Optional<LineStation> preLineStation = lineStations.stream()
                .filter(it -> it.getPreStationId() == null)
                .findFirst();

        List<LineStation> result = new ArrayList<>();
        while (preLineStation.isPresent()) {
            LineStation preStationId = preLineStation.get();
            result.add(preStationId);
            preLineStation = lineStations.stream()
                    .filter(it -> it.getPreStationId() == preStationId.getStationId())
                    .findFirst();
        }
        return result;
    }

    public void add(LineStation lineStation) {
        checkValidation(lineStation);

        lineStations.stream()
                .filter(it -> it.getPreStationId() == lineStation.getPreStationId())
                .findFirst()
                .ifPresent(it -> it.updatePreStationTo(lineStation.getStationId()));

        lineStations.add(lineStation);
    }

    private void checkValidation(LineStation lineStation) {
        if (lineStation.getStationId() == null) {
            throw new RuntimeException();
        }

        if (lineStations.stream().anyMatch(it -> it.isSame(lineStation))) {
            throw new RuntimeException();
        }
    }

    public void removeByStationId(Long stationId) {
        LineStation lineStation = lineStations.stream()
                .filter(it -> it.getStationId() == stationId)
                .findFirst()
                .orElseThrow(RuntimeException::new);

        lineStations.stream()
                .filter(it -> it.getPreStationId() == stationId)
                .findFirst()
                .ifPresent(it -> it.updatePreStationTo(lineStation.getPreStationId()));

        lineStations.remove(lineStation);
    }

    public Duration calculateDurationFromStartByDirection(Long stationId, PathDirection direction) {
        if (Objects.isNull(stationId)) {
            return Duration.ZERO;
        }
        List<LineStation> stationsInOrder = getStationsInOrder(direction);

        long totalDuration = 0;
        for (LineStation lineStation : stationsInOrder) {
            if (Objects.equals(lineStation.getPreStationId(), stationId)) {
                break;
            }
            totalDuration += lineStation.getDuration();
        }
        return Duration.ofMinutes(totalDuration);
    }

    private List<LineStation> getStationsInOrder(PathDirection direction) {
        if (direction == PathDirection.FORWARD) {
            return getStationsInOrder();
        }
        List<LineStation> stationsInOrder = getStationsInOrder();
        Collections.reverse(stationsInOrder);
        return stationsInOrder;


    }

}

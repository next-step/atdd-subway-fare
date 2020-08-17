package nextstep.subway.maps.line.domain;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import nextstep.subway.maps.map.domain.PathDirection;

@Embeddable
public class LineStations {
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "line_id", foreignKey = @ForeignKey(name = "fk_line_station_to_line"))
    private List<LineStation> lineStations = new ArrayList<>();

    public List<LineStation> getLineStations() {
        return lineStations;
    }

    protected List<LineStation> getStationsInOrder() {
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

    private List<LineStation> getStationsInOrder(PathDirection direction) {
        if (direction == PathDirection.UPBOUND) {
            return getStationsInOrder();
        }
        List<LineStation> stationsInOrder = getStationsInOrder();
        Collections.reverse(stationsInOrder);
        return stationsInOrder;
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

    public Duration calculateDurationFromStartByDirection(Long stationId, PathDirection pathDirection) {
        if (Objects.isNull(stationId)) {
            return Duration.ZERO;
        }
        List<LineStation> stationsInOrder = getStationsInOrder(pathDirection);
        long totalDuration = 0;
        for (LineStation lineStation : stationsInOrder) {
            if (isStartStation(stationId, lineStation, pathDirection)) {
                break;
            }
            totalDuration += lineStation.getDuration();
        }
        return Duration.ofMinutes(totalDuration);
    }

    private boolean isStartStation(Long stationId, LineStation lineStation, PathDirection pathDirection) {
        Long startStationId = null;
        if (pathDirection == PathDirection.UPBOUND) {
            startStationId = lineStation.getPreStationId();
            return Objects.equals(startStationId, stationId);
        }
        if (pathDirection == PathDirection.DOWNBOUND) {
            return Objects.equals(startStationId, stationId);
        }
        throw new IllegalStateException("something goes wrong");
    }
}

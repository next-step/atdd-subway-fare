package nextstep.subway.maps.line.domain;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public Duration calculateDurationFromStart(Long stationId) {
        if (Objects.isNull(stationId)) {
            return Duration.ZERO;
        }
        List<LineStation> stationsInOrder = getStationsInOrder();

        long totalDuration = 0;
        for (LineStation lineStation : stationsInOrder) {
            totalDuration += lineStation.getDuration();
            if (Objects.equals(lineStation.getStationId(), stationId)) {
                break;
            }
        }
        return Duration.ofMinutes(totalDuration);
    }
}

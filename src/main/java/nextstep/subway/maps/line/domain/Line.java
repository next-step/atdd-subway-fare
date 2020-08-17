package nextstep.subway.maps.line.domain;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import nextstep.subway.config.BaseEntity;
import nextstep.subway.maps.map.domain.PathDirection;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;
    private LocalTime startTime;
    private LocalTime endTime;
    private int intervalTime;
    private Money extraFare;
    @Embedded
    private LineStations lineStations = new LineStations();

    public Line() {
    }

    public Line(String name, String color, LocalTime startTime, LocalTime endTime, int intervalTime) {
        this.name = name;
        this.color = color;
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalTime = intervalTime;
    }

    public Line(String name, String color, LocalTime startTime, LocalTime endTime, int intervalTime, int extraFare) {
        this.name = name;
        this.color = color;
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalTime = intervalTime;
        this.extraFare = Optional.of(Money.drawNewMoney(extraFare)).orElse(Money.NO_VALUE());
    }

    public void update(Line line) {
        this.name = line.getName();
        this.startTime = line.getStartTime();
        this.endTime = line.getEndTime();
        this.intervalTime = line.getIntervalTime();
        this.color = line.getColor();
    }

    public void addLineStation(LineStation lineStation) {
        lineStations.add(lineStation);
    }

    public void removeLineStationById(Long stationId) {
        lineStations.removeByStationId(stationId);
    }

    public LocalTime calculateNextDepartureTime(Long stationId, LocalTime departTime, PathDirection pathDirection) {
        Duration duration = calculateDuration(stationId, pathDirection);
        LocalTime nextTime = startTime.plusMinutes(duration.toMinutes());
        while (nextTime.isBefore(departTime)) {
            nextTime = nextTime.plusMinutes(intervalTime);
        }
        return nextTime;
    }

    private Duration calculateDuration(Long stationId, PathDirection pathDirection) {
        return lineStations.calculateDurationFromStartByDirection(stationId, pathDirection);
    }

    public List<LineStation> getStationInOrder() {
        return lineStations.getStationsInOrder();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public int getIntervalTime() {
        return intervalTime;
    }

    public Money getExtraFare() {
        return extraFare;
    }
}

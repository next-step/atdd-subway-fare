package nextstep.subway.maps.line.domain;

import com.google.common.collect.Lists;
import nextstep.subway.config.BaseEntity;

import javax.persistence.*;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

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
    @Embedded
    private LineStations lineStations = new LineStations();
    private int extraFare;

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
        this(name, color, startTime, endTime, intervalTime);
        this.extraFare = extraFare;
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

    public List<LineStation> getStationInOrder() {
        return lineStations.getStationsInOrder();
    }

    public List<LineStation> getStationsInReverseOrder() {
        return Lists.reverse(this.getStationInOrder());
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

    public LineStations getLineStations() {
        return lineStations;
    }

    public int getExtraFare() {
        return this.extraFare;
    }

    private static boolean isShutdown(LocalTime start, LocalTime end, LocalTime currentTime) {
        if (start.equals(currentTime) || end.equals(currentTime)) {
            return false;
        }

        if (start.isAfter(end)) {
            return currentTime.isBefore(start) && currentTime.isAfter(end);
        } else {
            return currentTime.isBefore(start) || currentTime.isAfter(end);
        }
    }

    public LocalTime calculateForwardDepartureTime(LocalTime time) {

        if (isShutdown(startTime, endTime, time)) {
            throw new SubwayNotOperatingTimeException();
        }

        LocalTime departureTime = this.startTime;
        while (departureTime.isBefore(time)) {
            departureTime = departureTime.plus(intervalTime, ChronoUnit.MINUTES);
        }

        return departureTime;
    }

    public LocalTime calculateReverseDepartureTime(LocalTime time) {
        int forwardTotalDuration = sumTotalDuration();

        LocalTime startTimeReverse = startTime.plus(forwardTotalDuration, ChronoUnit.MINUTES);
        LocalTime endTimeReverse = endTime.plus(forwardTotalDuration, ChronoUnit.MINUTES);

        if (isShutdown(startTimeReverse, endTimeReverse, time)) {
            throw new SubwayNotOperatingTimeException();
        }

        if (isNextDay(time, endTimeReverse)) {
            LocalTime result = endTimeReverse;
            LocalTime temp = result;
            while(result.isAfter(time)) {
                temp = result;
                result = result.minus(intervalTime, ChronoUnit.MINUTES);
            }

            return temp;
        }

        LocalTime result = startTimeReverse;
        while (result.isBefore(time)) {
            result = result.plus(intervalTime, ChronoUnit.MINUTES);
        }

        return result;
    }

    private boolean isNextDay(LocalTime time, LocalTime endTimeReverse) {
        return time.isAfter(LocalTime.MIDNIGHT) && time.isBefore(endTimeReverse);
    }

    private int sumTotalDuration() {
        List<LineStation> stationInOrder = this.getStationInOrder();
        return stationInOrder.stream()
                .skip(1)
                .mapToInt(LineStation::getDuration)
                .sum();
    }
}

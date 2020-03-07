package atdd.path.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Station {
    private Long id;
    private String name;
    private List<Line> lines = new ArrayList<>();

    public Station() {
    }

    public Station(String name) {
        this.name = name;
    }

    public Station(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Station(Long id, String name, List<Line> lines) {
        this.id = id;
        this.name = name;
        this.lines = lines;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    @JsonIgnore
    public List<Line> getLines() {
        return lines;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return Objects.equals(id, station.id) &&
                Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public LocalTime calculateStartTime(Line line, int index) {
        int intervalOfLine = line.getInterval();
        int minutesFromLineStartTIme = (intervalOfLine) * (index);
        LocalTime startTime = line.getStartTime().plusMinutes(minutesFromLineStartTIme);
        return startTime;
    }

    public LocalTime calculateEndTime(Line line, int index) {
        int intervalOfLine = line.getInterval();
        int minutesFromLineEnd = (intervalOfLine) * index;
        LocalTime endTime = line.getEndTime().plusMinutes(minutesFromLineEnd);
        return endTime;
    }

    public TimeTables showTimeTablesForUpDown(Line line, List<Station> stations) {
        int index = stations.indexOf(this);
        int reverseIndex = stations.size() - index - 1;
        Station firstStation = stations.get(0);

        List<LocalTime> timeTableUp = makeTimeTableForOneWay(line, firstStation, index);
        List<LocalTime> timeTableDown = makeTimeTableForOneWay(line, firstStation, reverseIndex);

        TimeTables timeTables = new TimeTables(timeTableUp, timeTableDown);
        return timeTables;
    }

    private List<LocalTime> makeTimeTableForOneWay(Line line, Station firstStationInPath, int indexOfStationInLine) {
        if (indexOfStationInLine == 0) {
            return line.makeTimeTable(line.getStartTime(), line.getEndTime());
        }

        LocalTime startTime = firstStationInPath.calculateStartTime(line, indexOfStationInLine);
        LocalTime endTime = firstStationInPath.calculateEndTime(line, indexOfStationInLine);

        List<LocalTime> timeTableForOneWay = line.makeTimeTable(startTime, endTime);
        return timeTableForOneWay;
    }
}

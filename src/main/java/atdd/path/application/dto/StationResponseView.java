package atdd.path.application.dto;

import atdd.path.domain.Line;
import atdd.path.domain.Station;
import atdd.path.domain.TimeTables;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StationResponseView {
    private Long id;
    private String name;
    private List<LineSimpleResponseView> lines;

    public StationResponseView() {
    }

    public StationResponseView(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public StationResponseView(Long id, String name, List<Line> lines) {
        this.id = id;
        this.name = name;
        this.lines = LineSimpleResponseView.listOf(lines);
    }

    public static StationResponseView of(Station station) {
        return new StationResponseView(station.getId(), station.getName(), station.getLines());
    }

    public static List<StationResponseView> listOf(List<Station> stations) {
        return stations.stream()
                .map(it -> new StationResponseView(it.getId(), it.getName()))
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @JsonIgnore
    public List<LineSimpleResponseView> getLines() {
        return lines;
    }

    public TimeTables showTimeTablesForUpDown(Line line, List<Station> stations) {
        int index = stations.indexOf(this);
        int reverseIndex = stations.size() - stations.indexOf(this) - 1;

        List<LocalTime> timeTableUp = new ArrayList<>();
        if (reverseIndex != 0) {
            LocalTime start
                    = stations.get(0).calculateStartTime(line, index);
            LocalTime end
                    = stations.get(0).calculateEndTime(line, index);
            timeTableUp = line.makeTimeTable(start, end);
        }

        List<LocalTime> timeTableDown = new ArrayList<>();
        if (index != 0) {
            LocalTime startReverse
                    = stations.get(0).calculateStartTime(line, reverseIndex);
            LocalTime endReverse
                    = stations.get(0).calculateEndTime(line, reverseIndex);
            timeTableDown = line.makeTimeTable(startReverse, endReverse);
        }

        TimeTables timeTables = new TimeTables(timeTableUp, timeTableDown);
        return timeTables;
    }

}

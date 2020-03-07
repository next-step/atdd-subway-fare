package atdd.path.application.dto;

import lombok.Getter;

@Getter
public class StationTimetablesResponseView {
    private long lineId;
    private String lineName;
    private TimetablesResponseView timetables;

    public StationTimetablesResponseView() {
    }
}

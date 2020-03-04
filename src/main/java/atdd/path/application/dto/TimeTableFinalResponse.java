package atdd.path.application.dto;

import java.util.List;

public class TimeTableFinalResponse {
    private List<TimeTableResponseView> timeTableResponseViews;

    public TimeTableFinalResponse() {
    }

    public TimeTableFinalResponse(List<TimeTableResponseView> timeTableResponseViews) {
        this.timeTableResponseViews = timeTableResponseViews;
    }

    public List<TimeTableResponseView> getTimeTableResponseViews() {
        return timeTableResponseViews;
    }
}

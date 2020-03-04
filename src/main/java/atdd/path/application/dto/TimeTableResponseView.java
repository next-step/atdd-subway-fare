package atdd.path.application.dto;

import atdd.path.application.domain.TimeTables;

public class TimeTableResponseView {
    private Long lineId;
    private String lineName;
    private TimeTables timeTables;

    public TimeTableResponseView() {
    }

    public TimeTableResponseView(Long lineId, String lineName, TimeTables timeTables) {
        this.lineId = lineId;
        this.lineName = lineName;
        this.timeTables = timeTables;
    }
}

package atdd.path.application.dto;

import atdd.path.domain.TimeTables;

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

    public Long getLineId() {
        return lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public TimeTables getTimeTables() {
        return timeTables;
    }
}

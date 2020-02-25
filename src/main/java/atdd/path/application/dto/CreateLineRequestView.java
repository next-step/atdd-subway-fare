package atdd.path.application.dto;

import atdd.path.domain.Line;

import java.time.LocalTime;

public class CreateLineRequestView {
    private String name;
    private String startTime;
    private String endTime;
    private int interval;

    public CreateLineRequestView(String name, String startTime, String endTime, int interval) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.interval = interval;
    }

    public Line toLine() {
        return Line.of(name, LocalTime.parse(startTime), LocalTime.parse(endTime), interval);
    }

    public String getName() {
        return name;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public int getInterval() {
        return interval;
    }
}

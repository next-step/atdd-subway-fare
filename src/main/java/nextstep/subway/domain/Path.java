package nextstep.subway.domain;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class Path {
    private final Sections sections;
    private LocalDateTime arrivalTime;

    public Path(Sections sections) {
        this.sections = sections;
    }

    public Sections getSections() {
        return sections;
    }

    public int extractDistance() {
        return sections.totalDistance();
    }

    public int extractDuration() {
        return sections.totalDuration();
    }

    public void applyArrivalTime(LocalDateTime takeTime) {
        this.arrivalTime = sections.arrivalTime(takeTime);
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }
}

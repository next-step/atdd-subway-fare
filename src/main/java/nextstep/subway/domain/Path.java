package nextstep.subway.domain;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class Path {
    private Sections sections;

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

    public List<Station> getStations() {
        return sections.getStations();
    }

    public int maxAdditionalFare() {
        return sections.maxAdditionalFare();
    }

    public LocalDateTime getArrivalTime(LocalDateTime departureDate) {
        Line boardingLine = null;
        List<Section> sectionList = sections.getSections();
        for (Section section : sectionList) {
            if (section.isTransfer(boardingLine)) {
                boardingLine = section.getLine();
                long waitingTime = checkWaitingTime(section, departureDate);
                departureDate = departureDate.plusMinutes(waitingTime);
            }
            int durationTime = section.getDuration();
            departureDate = departureDate.plusMinutes(durationTime);
        }
        return departureDate;
    }

    private long checkWaitingTime(Section section, LocalDateTime departureDate) {
        LocalDateTime nextSchedule = section.getNextSchedule(departureDate);
        if (!departureDate.isEqual(nextSchedule)) {
            Duration duration = Duration.between(departureDate, nextSchedule);
            return duration.toMinutes();
        }
        return 0L;
    }
}

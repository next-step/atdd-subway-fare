package nextstep.subway.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Path {
    private Sections sections;
    private Fare fare;
    private ArrivalTime arrivalTime;

    public Path(Sections sections, Fare fare) {
        this(sections, fare, null);
    }

    public Path(Path path, ArrivalTime arrivalTime) {
        this(path.sections, path.fare, arrivalTime);
    }

    public Path(Sections sections, Fare fare, ArrivalTime arrivalTime) {
        this.sections = sections;
        this.fare = fare;
        this.arrivalTime = arrivalTime;
    }

    public Path(Sections sections) {
        this(sections, new Fare(sections.totalDistance()));
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    public int extractDistance() {
        return sections.totalDistance();
    }

    public int extractDuration() {
        return sections.totalDuration();
    }

    public int getFare() {
        return fare.value();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public LocalDateTime getArrivalTime() {
        if (arrivalTime == null) {
            return null;
        }
        return arrivalTime.value();
    }
}

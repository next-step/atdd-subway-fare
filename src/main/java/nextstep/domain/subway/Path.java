package nextstep.domain.subway;

import nextstep.util.FareCalculator;

import java.util.List;

public class Path {
    private Sections sections;
    private Long distance;
    private Long duration;
    private int fare;

    public Path(Sections sections) {
        this.sections = sections;
        this.distance = sections.totalDistance();
        this.duration = sections.totalDuration();
        this.fare = FareCalculator.totalFare(this.distance,sections.getLines());

    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public Long getDistance() {
        return this.distance;
    }

    public Long getDuration() {
        return this.duration;
    }

    public int getFare() {
        return this.fare;
    }

}

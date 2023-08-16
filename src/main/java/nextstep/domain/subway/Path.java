package nextstep.domain.subway;

import nextstep.domain.member.Member;
import nextstep.util.FareCalculator;

import java.util.List;

public class Path {
    private Sections sections;
    private Long distance;
    private Long duration;


    public Path(Sections sections) {
        this.sections = sections;
        this.distance = sections.totalDistance();
        this.duration = sections.totalDuration();
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

    public int getFare(int age) {
        return FareCalculator.totalFare(this.distance, sections.getLines(), age);
    }

}

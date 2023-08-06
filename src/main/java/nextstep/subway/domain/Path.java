package nextstep.subway.domain;

import nextstep.member.domain.Member;
import nextstep.subway.domain.fare.SubwayFare;

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

    public List<Station> getStations() {
        return sections.getStations();
    }

    public int extractDuration() {
        return sections.totalDuration();
    }

    public int fare(Member member) {
        return SubwayFare.calculateFare(this, member);
    }
}

package nextstep.subway.domain;

import nextstep.member.domain.Member;

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

    public int calculateFare(Member member, MemberFarePolicy farePolicy) {
        return farePolicy.calculate(member, this);
    }
}

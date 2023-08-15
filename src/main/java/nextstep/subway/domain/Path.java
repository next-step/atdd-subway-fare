package nextstep.subway.domain;

import nextstep.member.domain.Member;

import java.util.List;

public class Path {
    private final Sections sections;
    private final Fare fare;

    public Path(Sections sections, Fare fare) {
        this.sections = sections;
        this.fare = fare;
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

    public int getPrice() {
        return fare.getFare();
    }

    public void discountFare(Member member) {
        fare.discount(member);
    }
}

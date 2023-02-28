package nextstep.subway.domain;

import nextstep.member.domain.Member;
import nextstep.subway.domain.policy.FarePolicies;

import java.util.List;
import java.util.Optional;

public class Path {
    private final Sections sections;
    private final Optional<Member> member;
    private final FarePolicies farePolicies = new FarePolicies();

    public Path(Sections sections, Optional<Member> member) {
        this.sections = sections;
        this.member = member;
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

    public int calculateFare(){
        return this.farePolicies.calculate(this.extractDistance(), member);
    }
}

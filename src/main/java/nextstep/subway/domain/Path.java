package nextstep.subway.domain;

import nextstep.member.domain.Member;
import nextstep.subway.domain.fee.Fee;

import java.util.List;

public class Path {
    private final Sections sections;

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

    public int getFee(Member member) {
        Fee fee = new Fee(sections.totalDistance(), sections.getLines(), member);
        return fee.calculateFee();
    }

}

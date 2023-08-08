package nextstep.subway.domain;

import nextstep.member.domain.Member;

import java.util.List;

public class Path {
    private final Sections sections;
    private Member member;

    public Path(Sections sections, Member member) {
        this.sections = sections;
        this.member = member;
    }

    public Path(Sections sections) {
        this(sections, null);
    }

    public Sections getSections() {
        return sections;
    }

    public int extractDistance() {
        return sections.totalDistance();
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public int extractDuration() {
        return sections.totalDuration();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public int getFee() {
        int totalDistance = sections.totalDistance();
        int lineSurcharge = sections.getLineSurcharge();
        int distanceFee = DistanceFeeType.getDistanceFee(totalDistance).calculateFee(totalDistance);
        int totalFee = lineSurcharge + distanceFee;
        if (member != null) {
            return AgeFeeType.getAgeFeeTypeByAge(member.getAge()).getDiscountFee(totalFee);
        }
        return totalFee;
    }

}

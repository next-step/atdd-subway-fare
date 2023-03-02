package nextstep.subway.domain;

import nextstep.member.application.dto.MemberResponse;

import java.util.List;

public class Path {

    private final Sections sections;
    private final Fare fare;

    public Path(Sections sections) {
        this.sections = sections;
        fare = new Fare();
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

    public int calcFare() {
        return fare.calcFare();
    }

    public void addPolicy(FarePolicy farePolicy) {
        fare.addPolicy(farePolicy);
    }

    public void calcFareForNotMember() {
        int maxLineExtraFare = sections.getMaxExtraFare();
        addPolicy(new DistanceFarePolicy(extractDistance()));
        addPolicy(new ExtraSectionFarePolicy(maxLineExtraFare));
    }

    public void calcFareForMember(MemberResponse member) {
        int maxLineExtraFare = sections.getMaxExtraFare();
        addPolicy(new DistanceFarePolicy(extractDistance()));
        addPolicy(new ExtraSectionFarePolicy(maxLineExtraFare));
        addPolicy(new AgeFarePolicy(member.getAge()));
    }

}

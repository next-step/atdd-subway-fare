package nextstep.subway.domain;

import nextstep.member.domain.LoginMember;
import nextstep.subway.domain.fare.DiscountFareByAgeGroup;
import nextstep.subway.domain.fare.DistanceFare;
import nextstep.subway.domain.fare.FareHandler;
import nextstep.subway.domain.fare.LineWithExtraFarePolicy;

import java.util.List;

public class Path {
    private final Sections sections;

    private final FareHandler fareHandler;

    public Path(Sections sections, FareHandler fareHandler) {
        this.sections = sections;
        this.fareHandler = fareHandler;
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

    public int calculateFare() {
        return fareHandler.calculateFare();
    }

    public void calculateFare(LoginMember loginMember) {
        fareHandler.addPolicy(new DistanceFare(extractDistance()));
        fareHandler.addPolicy(new LineWithExtraFarePolicy(sections.getAdditionalFare()));
        fareHandler.addPolicy(new DiscountFareByAgeGroup(loginMember.getAge()));
    }
}

package nextstep.subway.domain;

import nextstep.member.application.dto.MemberResponse;
import nextstep.subway.domain.fare.AgeDiscountFareHandler;
import nextstep.subway.domain.fare.DefaultFareHandler;
import nextstep.subway.domain.fare.ExtraFareLineFareHandler;
import nextstep.subway.domain.fare.FareHandler;
import nextstep.subway.domain.fare.OverFiftyKiloFareHandler;
import nextstep.subway.domain.fare.TenKiloToFiftyKiloFareHandler;

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

    public int extractFare(MemberResponse loginMember) {
        FareHandler fareChain = new AgeDiscountFareHandler(new DefaultFareHandler(new TenKiloToFiftyKiloFareHandler(new OverFiftyKiloFareHandler(new ExtraFareLineFareHandler(null, sections)))), loginMember);
        int totalDistance = extractDistance();
        Fare totalFare = fareChain.handle(totalDistance);
        return totalFare.getFare();
    }

}

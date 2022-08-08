package nextstep.subway.domain;

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

    public int extractFare(int userAge) {
        int higherSurCharge = sections.getHigherSurCharge();
        int fare = Fare.calculate(extractDistance()) + higherSurCharge;
        int discountedFare = DiscountPolicy.calculate(userAge, fare);
        return discountedFare;
    }

    public int extractDuration() {
        return sections.totalDuration();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}

package nextstep.subway.domain;

import nextstep.subway.util.discount.DiscountAgePolicy;
import nextstep.subway.util.fare.FarePolicy;
import nextstep.subway.util.fare.NormalFarePolicy;

import java.util.List;

public class Path {

    private Sections sections;
    private FarePolicy farePolicy;
    private DiscountAgePolicy discountAgePolicy;

    public Path(Sections sections, DiscountAgePolicy discountAgePolicy) {
        this.sections = sections;
        this.farePolicy = new NormalFarePolicy();
        this.discountAgePolicy = discountAgePolicy;
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

    public int extractFare() {
        int totalFare = farePolicy.calculateFare(extractDistance()) + calculateMostExpensiveLine();

        return discountFare(totalFare);
    }

    public int calculateMostExpensiveLine() {
        return sections.mostExpensiveLineFare();
    }

    private int discountFare(int totalFare) {
        return discountAgePolicy.discount(totalFare);
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}

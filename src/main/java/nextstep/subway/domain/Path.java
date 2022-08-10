package nextstep.subway.domain;

import nextstep.subway.util.Age;
import nextstep.subway.util.DiscountPolicy;
import nextstep.subway.util.FarePolicy;
import nextstep.subway.util.NormalFarePolicy;
import nextstep.subway.util.UserDiscountPolicyByAge;

import java.util.List;

public class Path {

    private Sections sections;
    private FarePolicy farePolicy;
    private DiscountPolicy discountPolicy;

    public Path(Sections sections, Age age) {
        this.sections = sections;
        this.farePolicy = new NormalFarePolicy();
        this.discountPolicy = new UserDiscountPolicyByAge(age);
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
        return sections.mostExpensiveLinePrice();
    }

    private int discountFare(int totalFare) {
        return discountPolicy.discount(totalFare);
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}

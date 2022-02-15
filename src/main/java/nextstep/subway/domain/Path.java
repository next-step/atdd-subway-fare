package nextstep.subway.domain;

import java.util.List;

public class Path {
    private Sections sections;
    private DiscountPolicy discountPolicy;

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

    public int fare() {
        return FareType.fare(extractDistance());
    }

    public int extraCharge() {
        List<Line> lines = sections.containsLines();
        return lines.stream()
                .mapToInt(Line::getExtraCharge)
                .max()
                .orElse(0);
    }

    public void addDiscountPolicy(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    public int discount() {
        return discountPolicy.applyDiscount(fare());
    }

    public int totalFare() {
        return fare() + extraCharge() - discount();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}

package nextstep.subway.domain;

import java.util.List;
import java.util.Optional;

public class Path {
    private Sections sections;
    private FarePolicy farePolicy;
    private Optional<DiscountPolicy> discountPolicy;

    private int addFare;
    private int minDistance;

    public Path(final Sections sections,
                final FarePolicy farePolicy,
                final DiscountPolicy discountPolicy,
                final int addFare) {
        this(sections, farePolicy, discountPolicy, addFare ,0);
    }

    public Path(final Sections sections,
                final FarePolicy farePolicy,
                final DiscountPolicy discountPolicy,
                final int addFare,
                final int minDistance) {
        this.sections = sections;
        this.farePolicy = farePolicy;
        this.discountPolicy = Optional.ofNullable(discountPolicy);
        this.addFare = addFare;
        this.minDistance = minDistance;
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

    public int getFare() {
        if (minDistance > 0) {
            return calculateFare(minDistance);
        }
        return calculateFare(sections.totalDistance());
    }

    private int calculateFare(final int minDistance) {
        return getDiscountFare(farePolicy.calculator(minDistance) + addFare);
    }

    private int getDiscountFare(final int fare) {
        return discountPolicy.map(policy -> policy.calculator(fare)).orElse(fare);

    }
}

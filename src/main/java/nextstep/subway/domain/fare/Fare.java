package nextstep.subway.domain.fare;

import nextstep.subway.domain.fare.discount.DiscountFarePolicy;
import nextstep.subway.domain.fare.distance.DistanceFarePolicy;
import nextstep.subway.domain.line.Sections;

public class Fare {

    private final int distance;
    private final Sections sections;
    private final Integer age;

    public Fare(int distance, Sections sections, Integer age) {
        this.distance = distance;
        this.sections = sections;
        this.age = age;
    }

    public int calculate() {
        int fare = DistanceFarePolicy.create(this.distance).calculateFare() + lineExtraFare();
        return DiscountFarePolicy.create(fare, this.age).discount();
    }

    public int lineExtraFare() {
        return this.sections.sections()
                .stream()
                .mapToInt(section -> section.getLine().getAddFare())
                .filter(section -> section >= 0)
                .max().orElse(0);
    }

}

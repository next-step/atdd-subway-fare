package nextstep.subway.domain.path;

import lombok.Getter;
import nextstep.subway.domain.fare.discount.DiscountFarePolicy;
import nextstep.subway.domain.fare.distance.DistanceFarePolicy;
import nextstep.subway.domain.line.Sections;
import nextstep.subway.domain.station.Station;

import java.util.List;

@Getter
public class Path {
    private Sections sections;
    private int age;

    private Path(Sections sections, int age) {
        this.sections = sections;
        this.age = age;
    }

    public static Path of(Sections sections, int age) {
        return new Path(sections, age);
    }

    public int extractDistance() {
        return sections.totalDistance();
    }

    public int extractDuration() {
        return sections.totalDuration();
    }

    public int lineExtraFare() {
        return sections.lineExtraFare();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public int extractFare() {
        DistanceFarePolicy distanceFarePolicy = DistanceFarePolicy.create(extractDistance());
        int fare = distanceFarePolicy.calculateFare() + getSections().lineExtraFare();
        DiscountFarePolicy discountFarePolicy = DiscountFarePolicy.create(fare, getAge());
        return discountFarePolicy.discount();
    }

}

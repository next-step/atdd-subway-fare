package nextstep.subway.domain;

import lombok.Getter;
import nextstep.member.domain.Member;

import java.util.Collections;
import java.util.List;

@Getter
public class Path {

    private final List<Station> stations;

    private final int totalDistance;

    private final int totalDuration;

    private final int maxExtraFare;

    private Path(List<Station> stations, int totalDistance, int totalDuration, int maxExtraFare) {
        this.stations = Collections.unmodifiableList(stations);
        this.totalDistance = totalDistance;
        this.totalDuration = totalDuration;
        this.maxExtraFare = maxExtraFare;
    }

    public static Path of(Sections sections) {
        return new Path(
                sections.getStations(),
                sections.totalDistance(),
                sections.totalDuration(),
                sections.getMaxExtraFare()
        );
    }

    public Fare calculateFare() {
        return Fare.of(
                new DistanceFarePolicy(this.maxExtraFare),
                this.totalDistance
        );
    }

    public Fare calculateFare(Member member) {
        FarePolicy ageDiscountFarePolicy = AgeDiscountFarePolicyFactory.getPolicy(member.getAge());

        return Fare.of(
                new DistanceFarePolicy(
                        ageDiscountFarePolicy,
                        this.maxExtraFare
                ),
                this.totalDistance
        );
    }
}

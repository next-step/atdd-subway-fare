package nextstep.subway.path.domain.fare;

import nextstep.subway.path.domain.fare.age.AdultFareChain;
import nextstep.subway.path.domain.fare.age.AgeChain;
import nextstep.subway.path.domain.fare.age.ChildFareChain;
import nextstep.subway.path.domain.fare.age.YouthFareChain;
import nextstep.subway.path.domain.fare.distance.Distance10FareChain;
import nextstep.subway.path.domain.fare.distance.Distance50FareChain;
import nextstep.subway.path.domain.fare.distance.DistanceDefaultFareChain;
import nextstep.subway.path.domain.fare.distance.DistanceFareChain;

import java.util.Arrays;
import java.util.List;

public class FareCalculator {

    private List<DistanceFareChain> distanceChains;
    private List<AgeChain> ageChains;

    public FareCalculator() {
        distanceChains = Arrays.asList(
                new DistanceDefaultFareChain(),
                new Distance10FareChain(),
                new Distance50FareChain());

        ageChains = Arrays.asList(
                new AdultFareChain(),
                new YouthFareChain(),
                new ChildFareChain());
    }

    public int calculate(int distance, int age) {
        int fare = calculateDistance(distance);
        return calculateAge(age, fare);
    }

    private int calculateDistance(int distance) {
        return distanceChains.stream()
                .mapToInt(chain -> chain.calculate(distance))
                .sum();
    }

    private int calculateAge(int age, int fare) {
        return ageChains.stream()
                .filter(chain -> chain.findAgeGroup(age))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("유효하지 않은 나이 값입니다"))
                .calculate(fare);
    }
}

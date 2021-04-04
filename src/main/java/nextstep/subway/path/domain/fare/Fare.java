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

public class Fare {

    private int fare;

    public Fare(int distance) {
        if(distance < 0) {
            throw new RuntimeException("요금은 양수이어야 합니다");
        }
        calculateDistance(distance);
    }

    public Fare(int distance, int age) {
        if(distance < 0) {
            throw new RuntimeException("요금은 양수이어야 합니다");
        }
        if(age < 0) {
            throw new RuntimeException("나이는 양수이어야 합니다");
        }
        calculateDistance(distance);
        calculateAge(age);
    }

    private void calculateDistance(int distance) {
        List<DistanceFareChain> distanceChains = Arrays.asList(
                new DistanceDefaultFareChain(),
                new Distance10FareChain(),
                new Distance50FareChain());

        this.fare = distanceChains.stream()
                .mapToInt(chain -> chain.calculate(distance))
                .sum();
    }

    private void calculateAge(int age) {
        List<AgeChain> ageChains = Arrays.asList(
                new AdultFareChain(),
                new YouthFareChain(),
                new ChildFareChain());

        this.fare = ageChains.stream()
                .filter(chain -> chain.findAgeGroup(age))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("유효하지 않은 나이 값입니다"))
                .calculate(this.fare);
    }

    public int getFare() {
        return fare;
    }
}

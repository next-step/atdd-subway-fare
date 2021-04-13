package nextstep.subway.path.domain.fare.age;

import java.util.Arrays;
import java.util.List;

public class AgePolicy {

    private int age;
    private List<AgeChain> ageChains;

    public AgePolicy(int age) {
        this.age = age;
        this.ageChains = Arrays.asList(
                new AdultFareChain(),
                new YouthFareChain(),
                new ChildFareChain());
    }

    public int calculate(int fare) {
        if(age == 0) {
            return fare;
        }
        return ageChains.stream()
                .filter(chain -> chain.findAgeGroup(age))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("유효하지 않은 나이 값입니다"))
                .calculate(fare);
    }
}

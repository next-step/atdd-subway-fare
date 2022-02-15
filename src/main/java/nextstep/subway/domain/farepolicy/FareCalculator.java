package nextstep.subway.domain.farepolicy;

import java.util.Arrays;
import java.util.List;

import nextstep.subway.domain.Path;

public class FareCalculator implements FarePolicy {
    private final List<FarePolicy> farePolicies;

    public FareCalculator(List<FarePolicy> farePolicies) {
        this.farePolicies = farePolicies;
    }

    public FareCalculator() {
        this(Arrays.asList(
            new BasicFarePolicy(), // 기본 요금 정책
            new DistanceFarePolicy(new DistanceFareRange(10, 50), 5, 100), // 10km부터 50km 까지의 거리 정책
            new DistanceFarePolicy(new DistanceFareRange(50, Integer.MAX_VALUE), 8, 100), // 50km 초과의 거리 정책
            new LineAdditionalFarePolicy() // 노선별 추가 요금 정책
        ));
    }

    @Override
    public int calculate(Path path) {
        return farePolicies.stream()
                           .mapToInt(eachPolicy -> eachPolicy.calculate(path))
                           .sum();
    }
}

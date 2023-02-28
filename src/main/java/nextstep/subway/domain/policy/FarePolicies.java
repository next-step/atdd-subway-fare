package nextstep.subway.domain.policy;

import nextstep.member.domain.Member;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FarePolicies {
    private final List<FarePolicy> values;

    public FarePolicies() {

        FarePolicy base = new FixedFarePolicy(1250);
        FarePolicy ten = new BetweenUnitFarePolicy(10, 50, 5, 100);
        FarePolicy fifth = new GreaterUnitFarePolicy(50, 8, 100);

        values = Arrays.asList(base, ten, fifth);
    }


    public int calculate(int distance, Optional<Member> member) {
        return values.stream().map(policy -> policy.calculate(distance)).reduce(0, Integer::sum);
    }
}

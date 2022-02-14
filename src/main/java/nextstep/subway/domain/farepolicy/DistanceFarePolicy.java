package nextstep.subway.domain.farepolicy;

import java.util.ArrayList;
import java.util.List;

public class DistanceFarePolicy implements FarePolicy {
    private final List<FareCondition> conditions;

    public DistanceFarePolicy(List<FareCondition> conditions) {
        this.conditions = conditions;
    }

    @Override
    public int calculate(int distance) {
        return 0;
    }
}

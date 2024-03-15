package nextstep.core.subway.fare.application;

import nextstep.core.auth.domain.constants.AgeGroup;
import nextstep.core.subway.path.application.FareCalculationContext;
import org.springframework.stereotype.Component;

@Component
public class AgeFareCalculatePolicy implements FareCalculatePolicy {

    @Override
    public int apply(FareCalculationContext context) {
        if (AgeGroup.CHILDREN == context.getAgeGroup()) {
            return -(1250 - 450);
        }
        if (AgeGroup.TEENAGE == context.getAgeGroup()) {
            return -(1250 - 720);
        }
        return 0;
    }
}

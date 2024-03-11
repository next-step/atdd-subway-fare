package nextstep.core.subway.path.application;

import nextstep.core.auth.domain.constants.AgeGroup;
import org.springframework.stereotype.Component;

@Component
public class AgeFareCalculator {

    public int calculateAgeDiscount(AgeGroup ageGroup) {
        if(AgeGroup.CHILDREN == ageGroup) {
            return -(1250 - 450);
        }
        if(AgeGroup.TEENAGE == ageGroup) {
            return -(1250 - 720);
        }
        return 0;
    }
}

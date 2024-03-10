package nextstep.core.subway.path.application;

import org.springframework.stereotype.Component;

@Component
public class AgeFareCalculator {

    public int calculateAgeDiscount(int age) {
        if(age >= 6 && age <= 12) {
            return -(1250 - 450);
        }
        if(age >= 13 && age <= 18) {
            return -(1250 - 720);
        }
        return 0;
    }
}

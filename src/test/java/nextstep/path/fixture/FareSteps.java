package nextstep.path.fixture;

import nextstep.path.domain.Fare;

import java.util.Collections;

public class FareSteps {
    public static Fare buildFare(int distance) {
        return new Fare(Collections.emptyList(), distance);
    }

}

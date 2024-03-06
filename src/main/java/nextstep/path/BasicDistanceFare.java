package nextstep.path;

import nextstep.path.domain.Fare;
import org.springframework.stereotype.Component;

@Component
public class BasicDistanceFare implements DistanceFare {

    @Override
    public int calculateFare(Fare fare) {
        return 1_250;
    }

}

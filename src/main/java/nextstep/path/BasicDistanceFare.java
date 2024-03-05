package nextstep.path;

import org.springframework.stereotype.Component;

@Component
public class BasicDistanceFare implements DistanceFare {

    @Override
    public int calculateFare(int distance) {
        return 1_250;
    }

}

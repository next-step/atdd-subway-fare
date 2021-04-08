package nextstep.subway.path.application;

import nextstep.subway.path.domain.fare.FareCalculator;
import nextstep.subway.path.domain.fare.policy.DefaultEqualTo10Policy;
import nextstep.subway.path.domain.fare.policy.Over10EqualTo50Policy;
import nextstep.subway.path.domain.fare.policy.Over50Policy;
import org.springframework.stereotype.Service;

@Service
public class FareService {

    public int calculate(int distance) {
        FareCalculator fareCalculator = new FareCalculator();
        fareCalculator.addPolicy(new DefaultEqualTo10Policy());
        fareCalculator.addPolicy(new Over10EqualTo50Policy());
        fareCalculator.addPolicy(new Over50Policy());

        return fareCalculator.calculate(distance);
    }
}

package nextstep.subway.applicaion;

import nextstep.subway.domain.Path;
import nextstep.subway.domain.fare.FareCalculatorChain;
import org.springframework.stereotype.Service;

@Service
public class FareService {

    private final FareCalculatorChain fareCalculatorChain;

    public FareService(FareCalculatorChain fareCalculator) {
        this.fareCalculatorChain = fareCalculator;
    }

    public int calculate(Path path) {
        return fareCalculatorChain.calculate(path, 0);
    }
}

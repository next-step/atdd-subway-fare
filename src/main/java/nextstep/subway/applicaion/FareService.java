package nextstep.subway.applicaion;

import nextstep.subway.domain.fare.FareBasis;
import nextstep.subway.domain.fare.FarePolicies;
import org.springframework.stereotype.Service;

@Service
public class FareService {
    private final FarePolicies farePolicies;

    public FareService() {
        this.farePolicies = new FarePolicies();
    }

    public int totalFare(int distance, int lineFare, int age) {
        FareBasis fareBasis = new FareBasis(distance, age, lineFare);
        return farePolicies.calculateFare(fareBasis);
    }
}

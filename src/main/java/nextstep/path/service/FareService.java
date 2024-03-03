package nextstep.path.service;

import nextstep.path.DistanceFare;
import nextstep.path.DistanceFareFactory;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FareService {

    public int calculate(int distance) {
        List<DistanceFare> distanceFare = DistanceFareFactory.createDistanceFare(distance);
        int sum = 0;
        for (DistanceFare fare : distanceFare) {
            sum += fare.calculateFare();
        }
        return sum;
    }
}

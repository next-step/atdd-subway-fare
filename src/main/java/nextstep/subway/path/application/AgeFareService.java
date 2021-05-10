package nextstep.subway.path.application;

import nextstep.subway.path.domain.Fare;
import org.springframework.stereotype.Service;

@Service
public class AgeFareService implements FarePolicy {

    @Override
    public Fare calculate() {
        return null;
    }
}

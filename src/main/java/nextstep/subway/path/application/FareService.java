package nextstep.subway.path.application;

import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.path.domain.Fare;
import org.springframework.stereotype.Service;

@Service
public class FareService {

    public int calculate(int maxAdditionalFee, int totalDistance, LoginMember loginMember) {
        Fare fare = Fare.createInstance(maxAdditionalFee, totalDistance, loginMember);
        return fare.calculateCost();
    }
}

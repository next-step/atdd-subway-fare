package nextstep.subway.path.application;

import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.path.domain.Fare;
import org.springframework.stereotype.Service;

@Service
public class FareService {

    public Fare calculate(int totalDistance, int maxAdditionalFare, LoginMember loginMember) {
        Fare fare = Fare.createInstance(totalDistance, maxAdditionalFare);
        fare.calculateCost();

        if (!loginMember.isAnonymous()) {
            fare.discountByAge(loginMember.getAge());
        }

        return fare;
    }
}

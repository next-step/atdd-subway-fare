package nextstep.subway.path.domain;

import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.path.domain.enumeration.FareDistanceType;

public class Fare {

    private FarePolicy farePolicy;
    private int fare;

    public Fare(int totalDistance, int additionalFee) {
        if (totalDistance > 0) {
            this.fare = FareDistanceType.calculateFareFromDistance(totalDistance);
        }
    }

    public int getFare() {
        return this.fare;
    }

    public void setAgePolicy(LoginMember loginMember) {
        return ;
    }
}

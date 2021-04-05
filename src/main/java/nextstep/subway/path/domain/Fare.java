package nextstep.subway.path.domain;

import nextstep.subway.path.domain.policy.FarePolices;
import nextstep.subway.path.domain.policy.FarePolicy;

import static nextstep.subway.path.domain.policy.PolicyConstant.BASIC_FARE;

public class Fare {

    private FarePolices farePolices = new FarePolices();
    private int fare;

    public Fare() {
        fare = BASIC_FARE.of();
    }

    public void addFarePolicy(FarePolicy farePolicy){
        farePolices.addFarePolicy(farePolicy);
    }

    public int getFare(){
        farePolices.linkFarePolicy();
        return farePolices.calculateFare(fare);
    }
}

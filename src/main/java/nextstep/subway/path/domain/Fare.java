package nextstep.subway.path.domain;

import nextstep.subway.path.domain.policy.FarePolices;
import nextstep.subway.path.domain.policy.FarePolicy;

public class Fare {

    private FarePolices farePolices = new FarePolices();
    private int fare;

    public Fare() {
        fare = 0;
    }

    public void addFarePolicy(FarePolicy farePolicy){
        farePolices.addFarePolicy(farePolicy);
    }

    public int getFare(){
        farePolices.linkFarePolicy();
        return farePolices.calculateFare();
    }
}

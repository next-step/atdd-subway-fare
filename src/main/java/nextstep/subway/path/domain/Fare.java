package nextstep.subway.path.domain;

import nextstep.subway.path.domain.policy.FarePolices;

public class Fare {

    private FarePolices farePolices = new FarePolices();

    public Fare(FarePolices farePolices) {
        this.farePolices = farePolices;
    }

    public int getFare(){
        return farePolices.calculateFare();
    }
}

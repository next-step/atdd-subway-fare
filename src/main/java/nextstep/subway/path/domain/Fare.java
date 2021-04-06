package nextstep.subway.path.domain;

import nextstep.subway.path.domain.policy.FarePolices;

public class Fare {

    private FarePolices farePolices;

    public Fare(FarePolices farePolices) {
        this.farePolices = farePolices;
    }

    public int getFare(){
        return farePolices.calculateFare();
    }
}

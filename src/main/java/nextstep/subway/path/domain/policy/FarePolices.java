package nextstep.subway.path.domain.policy;

import java.util.LinkedList;

public class FarePolices {

    private LinkedList<FarePolicy> farePolicies = new LinkedList<>();

    public FarePolices(){

    }

    public void addFarePolicy(FarePolicy farePolicy){
        farePolicies.add(farePolicy);
    }

    public int calculateFare(){
        int fare = 0;
        for (FarePolicy farePolicy : farePolicies) {
            fare = farePolicy.apply(fare);
        }
        return fare;
    }

}

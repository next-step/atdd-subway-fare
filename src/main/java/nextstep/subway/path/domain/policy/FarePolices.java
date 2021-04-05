package nextstep.subway.path.domain.policy;

import java.util.LinkedList;

public class FarePolices {

    private LinkedList<FarePolicy> farePolicies = new LinkedList<>();

    public FarePolices(){

    }

    public void addFarePolicy(FarePolicy farePolicy){
        farePolicies.add(farePolicy);
    }

    public void linkFarePolicy(){
        FarePolicy cur = null;
        for (FarePolicy farePolicy : farePolicies) {
            if (cur == null) {
                cur = farePolicy;
            } else {
                cur.setNext(farePolicy);
                cur = farePolicy;
            }
        }
    }

    public int calculateFare(){
        if (farePolicies.isEmpty()) {
            return 0;
        }
        farePolicies.getFirst().apply();
        return farePolicies.getLast().getFare();
    }

}

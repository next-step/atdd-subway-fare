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
            }
        }
    }

    public int calculateFare(int fare){
        if (farePolicies.isEmpty()) {
            return fare;
        }
        farePolicies.getFirst().apply();
        return fare += farePolicies.stream().mapToInt(it -> it.getFare()).sum();
    }

}

package nextstep.subway.domain.fare;

public abstract class FarePolicy {

    private int policyOrder = Integer.MAX_VALUE;

    void setPolicyOrder(int policyOrder) {
        this.policyOrder = policyOrder;
    }
    int getPolicyOrder() {
        return policyOrder;
    }
    abstract int fare(int prevFare);
}

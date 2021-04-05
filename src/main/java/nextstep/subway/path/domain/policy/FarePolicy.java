package nextstep.subway.path.domain.policy;

public abstract class FarePolicy {

    protected int fare;
    protected FarePolicy next = null;

    public void setNext(FarePolicy next){
        this.next = next;
    }

    public void apply(){
        if (isValidate()) {
            calculate();
        }
        if (next != null) {
            next.setFare(fare);
            next.apply();
        }
    }

    abstract protected void calculate();

    abstract protected boolean isValidate();

    public int getFare(){
        return fare;
    }

    private void setFare(int fare) {this.fare = fare;}
}

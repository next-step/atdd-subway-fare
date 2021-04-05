package nextstep.subway.path.domain.policy;

public abstract class FarePolicy {

    protected int fare = 0;
    protected FarePolicy next = null;

    public void setNext(FarePolicy next){
        this.next = next;
    }

    public void apply(){
        if (isValidate()) {
            fare += calculate();
        }
        if (next != null) {
            next.apply();
        }
    }

    abstract protected int calculate();

    abstract protected boolean isValidate();

    public int getFare(){
        return fare;
    }
}

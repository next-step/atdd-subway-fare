package nextstep.subway.path.domain.policy;

public abstract class FarePolicy {

    protected int fare = 0;

    public int apply(int fare){
        setFare(fare);
        if (isValidate()) {
            calculate();
        }
        return getFare();
    }

    abstract protected void calculate();

    abstract protected boolean isValidate();

    public int getFare(){
        return fare;
    }

    private void setFare(int fare) {this.fare = fare;}
}

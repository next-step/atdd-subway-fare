package nextstep.subway.path.domain.policy;

public class BasePolicy extends FarePolicy {

    private static final int BASIC_FARE = 1250;

    public BasePolicy() {

    }

    @Override
    protected void calculate() {
        fare += BASIC_FARE;
    }

    @Override
    protected boolean isValidate() {
        return true;
    }

}

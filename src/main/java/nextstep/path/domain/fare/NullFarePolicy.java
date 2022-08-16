package nextstep.path.domain.fare;

public class NullFarePolicy extends FarePolicy {

    NullFarePolicy() {
        super(null);
    }

    @Override
    public int apply(int beforeFare) {
        return beforeFare;
    }
}

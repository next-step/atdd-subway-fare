package nextstep.path.domain.fare2;

public class NullFarePolicy extends FarePolicy {

    NullFarePolicy() {
        super(null);
    }

    @Override
    public int apply(int beforeFare) {
        return beforeFare;
    }
}

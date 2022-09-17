package nextstep.subway.domain;

public class Default extends DiscountFarePolicy {

    public Default(int fare) {
        super(fare);
    }

    @Override
    public int discount() {
        return getFare();
    }

}

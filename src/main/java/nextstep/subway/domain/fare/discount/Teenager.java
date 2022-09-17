package nextstep.subway.domain;

public class Teenager extends DiscountFarePolicy {

    public Teenager(int fare) {
        super(fare);
    }

    @Override
    public int discount() {
        return (int) ((getFare() - 350) * 0.8);
    }

}

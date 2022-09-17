package nextstep.subway.domain;

public class Children extends DiscountFarePolicy {

    public Children(int fare) {
        super(fare);
    }
    @Override
    public int discount() {
        return (int) ((getFare() - 350) * 0.5);
    }

}

package nextstep.subway.maps.boarding.domain;

public enum Age {

    CHILD(50),
    YOUTH(20),
    ADULT(0);

    private final int discountRate;

    Age(int discountRate) {
        this.discountRate = discountRate;
    }

    public int getDiscountRate() {
        return discountRate;
    }
}

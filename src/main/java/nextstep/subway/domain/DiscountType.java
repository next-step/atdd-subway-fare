package nextstep.subway.domain;

public enum DiscountType {

    YOUTH(13, 18, 20),
    CHILDREN(6, 12, 50);

    private final int minAge;
    private final int maxAge;
    private final int discountPercentage;

    DiscountType(int minAge, int maxAge, int discountPercentage) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.discountPercentage = discountPercentage;
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public boolean isAgeOf(int age) {
        return age >= this.minAge && age <= this.maxAge;
    }
}

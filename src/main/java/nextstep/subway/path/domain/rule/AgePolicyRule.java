package nextstep.subway.path.domain.rule;

public enum AgePolicyRule {

    KID(6, 13, 0.5),
    YOUTH(13, 19, 0.8);

    private final Integer minAge;
    private final Integer maxAge;
    private final Double discountRate;

    AgePolicyRule(Integer minAge, Integer maxAge, Double discountRate) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.discountRate = discountRate;
    }

    public Integer getMinAge() {
        return minAge;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public Double getDiscountRate() {
        return discountRate;
    }
}

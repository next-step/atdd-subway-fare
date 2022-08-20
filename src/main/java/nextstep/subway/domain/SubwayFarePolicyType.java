package nextstep.subway.domain;

public enum SubwayFarePolicyType {
    YOUTH(13, 18, 350, 0.2),
    CHILD(6, 12, 350, 0.5),
    INFANT(0, 5, 0, 1.0),
    ADULT(19, 100, 0, 0);

    private int minAge;
    private int maxAge;

    private int deductionFare;
    private double discountPercent;

    SubwayFarePolicyType(Integer minAge, Integer maxAge, int deductionFare, double discountPercent) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.deductionFare = deductionFare;
        this.discountPercent = discountPercent;
    }

    public static SubwayFarePolicyType byAge(Integer age){

        if(SubwayFarePolicyType.CHILD.getMinAge() > age && age >0){
            return SubwayFarePolicyType.INFANT;
        }
        if (SubwayFarePolicyType.CHILD.getMaxAge() >= age
                && SubwayFarePolicyType.CHILD.getMinAge() <= age) {
            return SubwayFarePolicyType.CHILD;
        }
        if (SubwayFarePolicyType.YOUTH.getMaxAge() >= age
                && SubwayFarePolicyType.YOUTH.getMinAge() <= age) {
            return SubwayFarePolicyType.YOUTH;
        }
        return SubwayFarePolicyType.ADULT;
    }

    public int getMinAge() {
        return minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public int getDeductionFare() {
        return deductionFare;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }


}

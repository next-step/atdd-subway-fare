package nextstep.subway.path.domain.fare;

public class FareDiscountByAge implements Fare {

    private final int totalFare;
    private final FareDiscountByAgePolicy fareDiscountByAgePolicy;

    public FareDiscountByAge(int totalFare, int age) {
        this.totalFare = totalFare;
        this.fareDiscountByAgePolicy = FareDiscountByAgePolicy.byAge(age);
    }

    @Override
    public int calculate() {
        return fareDiscountByAgePolicy.getOperation().apply(totalFare);
    }
}

package nextstep.subway.domain.fare;

public class TeenFareChain implements FareChain{

    private static final int TEEN_START_RANGE = 13;
    private static final int TEEN_END_RANGE = 18;
    private static final double TEEN_PERCENTAGE = 0.2;
    private FareChain defaultChain;

    @Override
    public void setNextChain(FareChain fareChain) {
        this.defaultChain = fareChain;
    }

    @Override
    public int calculateFare(int distance, int additionalFee, Integer age) {
        if (age != null && age >= TEEN_START_RANGE && age <= TEEN_END_RANGE) {
            int totalPrice = defaultChain.calculateFare(distance, additionalFee, age);
            return getTeenDiscountedPrice(totalPrice);
        }
        return defaultChain.calculateFare(distance, additionalFee, age);
    }

    private int getTeenDiscountedPrice(int price) {
        int discountedAmount = (int) ((price-350) * TEEN_PERCENTAGE);
        return price - discountedAmount;
    }
}

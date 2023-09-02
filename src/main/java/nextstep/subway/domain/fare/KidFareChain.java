package nextstep.subway.domain.fare;

public class KidFareChain implements FareChain {

    private static final int KID_START_RANGE = 6;
    private static final int KID_END_RANGE = 12;
    private static final double KID_PERCENTAGE = 0.5;
    private FareChain teenChain;

    @Override
    public void setNextChain(FareChain fareChain) {
        this.teenChain = fareChain;
    }

    @Override
    public int calculateFare(int distance, int additionalFee, Integer age) {
        if (age != null && age >= KID_START_RANGE && age <= KID_END_RANGE) {
            int totalPrice = teenChain.calculateFare(distance, additionalFee, age);
            System.out.println(totalPrice);
            return getKidDiscountPrice(totalPrice);
        }
        return teenChain.calculateFare(distance, additionalFee, age);
    }

    private int getKidDiscountPrice(int price) {
        int discountedAmount = (int) ((price-350) * KID_PERCENTAGE);
        return price - discountedAmount;
    }
}

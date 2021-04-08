package nextstep.subway.path.domain;

public class UserFarePolicy implements FarePolicy {
    private static final int FIVE = 5;
    private static final int SIX = 6;
    private static final int EIGHT = 8;
    private static final int TEN = 10;
    private static final int THIRTEEN = 13;
    private static final int TWENTY = 20;

    private static final int DISCOUNT_AMOUNT = 350;

    private int userAge;

    public UserFarePolicy(int userAge) {
        this.userAge = userAge;
    }

    @Override
    public int calculateFareByPolicy(int subtotal) {
        if(isChild()) {
            return (( subtotal - DISCOUNT_AMOUNT) * FIVE / TEN);
        }

        if(isTeenager()) {
            return (( subtotal - DISCOUNT_AMOUNT) * EIGHT / TEN);
        }

        return subtotal;
    }

    private boolean isTeenager() {
        return THIRTEEN <= userAge && userAge < TWENTY;
    }

    private boolean isChild() {
        return SIX <= userAge && userAge < THIRTEEN;
    }


}

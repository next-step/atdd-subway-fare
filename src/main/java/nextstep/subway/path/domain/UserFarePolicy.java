package nextstep.subway.path.domain;

import nextstep.subway.auth.infrastructure.SecurityContextHolder;
import nextstep.subway.path.exception.CannotParseUserAgeException;
import org.springframework.util.ObjectUtils;

import java.util.Map;

public class UserFarePolicy implements FarePolicy {
    private static final int FIVE = 5;
    private static final int SIX = 6;
    private static final int EIGHT = 8;
    private static final int TEN = 10;
    private static final int THIRTEEN = 13;
    private static final int TWENTY = 20;

    private static final int DISCOUNT_AMOUNT = 350;

    private static final String AGE = "age";

    private int userAge;

    public UserFarePolicy() {
        this.userAge = getUserAge();
    }

    private int getUserAge() {
        if(isAuthenticationExists()) {
            return parseUserAge();
        }
        return -1;
    }

    private boolean isAuthenticationExists() {
        return !ObjectUtils.isEmpty(SecurityContextHolder.getContext().getAuthentication());
    }

    private int parseUserAge() {
        try{
            Map<String, String> principal = (Map) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return Integer.parseInt(principal.get(AGE));
        } catch (Exception e) {
            throw new CannotParseUserAgeException();
        }
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

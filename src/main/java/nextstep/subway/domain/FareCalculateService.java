package nextstep.subway.domain;

import org.springframework.stereotype.Service;

import static nextstep.subway.domain.DiscountType.CHILDREN;
import static nextstep.subway.domain.DiscountType.YOUTH;
import static nextstep.subway.domain.Fare.BASIC_FARE;
import static nextstep.subway.domain.Fare.OVER_FARE;
import static nextstep.subway.domain.OverFareLevel.OVER_10KM;
import static nextstep.subway.domain.OverFareLevel.OVER_50KM;

@Service
public class FareCalculateService {

    private static final int BASE_DISCOUNT_AMOUNT = 350;

    public int calculateFareAmount(Path path) {
        return getBasicFare(path) + path.getLineAdditionalFare();
    }

    public int calculateFareAmount(Path path, int age) {
        int totalFare = getBasicFare(path) + path.getLineAdditionalFare();
        return totalFare - getDiscountAmount(totalFare, age);
    }

    private int getBasicFare(Path path) {
        int distance = path.extractDistance();
        if (distance <= OVER_10KM.getStart()) {
            return BASIC_FARE.getAmount();
        }
        if (distance <= OVER_50KM.getStart()) {
            return BASIC_FARE.getAmount()
                + calculateOverFare(distance - OVER_10KM.getStart(), OVER_10KM.getInterval());
        }
        return BASIC_FARE.getAmount()
            + calculateOverFare(OVER_50KM.getStart() - OVER_10KM.getStart(), OVER_10KM.getInterval())
            + calculateOverFare(distance - OVER_50KM.getStart(), OVER_50KM.getInterval());
    }

    private int calculateOverFare(int distance, int interval) {
        return (int) ((Math.ceil((distance - 1) / interval) + 1) * OVER_FARE.getAmount());
    }

    private int getDiscountAmount(int totalFare, int age) {
        if (YOUTH.isAgeOf(age)) {
            return calculateDiscount(totalFare, YOUTH.getDiscountPercentage());
        }
        if (CHILDREN.isAgeOf(age)) {
            return calculateDiscount(totalFare, CHILDREN.getDiscountPercentage());
        }
        return 0;
    }

    private int calculateDiscount(int totalFare, int percentage) {
        return (int) Math.ceil((totalFare - BASE_DISCOUNT_AMOUNT) * percentage / 100);
    }
}

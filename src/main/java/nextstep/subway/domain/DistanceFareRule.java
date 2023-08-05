package nextstep.subway.domain;

import nextstep.member.domain.Member;
import org.springframework.util.ObjectUtils;

public class DistanceFareRule implements FareCalculationRule {
    private FareCalculationRule nextRule;
    private static final int BASIC_DISTANCE = 10;
    private static final int DISTANCE_FIFTY = 50;
    private static final int CHARGE_DISTANCE_FIVE = 5;
    private static final int CHARGE_DISTANCE_EIGHT = 8;
    private static final int ADDITIONAL_CHARGE_RATE = 100;

    public void setNextRule(FareCalculationRule nextRule) {
        this.nextRule = nextRule;
    }

    @Override
    public int calculateFare(Path path, Member member, int totalFare) {
        int distance = path.getSections().totalDistance();
        validateDistance(distance);

        if (distance > BASIC_DISTANCE && distance <= DISTANCE_FIFTY) {
            totalFare += calculateOverFare(distance - BASIC_DISTANCE, CHARGE_DISTANCE_FIVE);
        }

        if (distance > DISTANCE_FIFTY) {
            int distanceOverFifty = distance - DISTANCE_FIFTY;
            totalFare += calculateOverFare(distance - BASIC_DISTANCE - distanceOverFifty, CHARGE_DISTANCE_FIVE);
            totalFare += calculateOverFare(distanceOverFifty, CHARGE_DISTANCE_EIGHT);
        }

        if (ObjectUtils.isEmpty(nextRule)) {
            return totalFare;
        } else {
            return nextRule.calculateFare(path, member, totalFare);
        }
    }

    private static void validateDistance(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("거리가 올바르지 않습니다.");
        }
    }

    private static int calculateOverFare(int distance, int chargeDistance) {
        return (int) ((Math.ceil((distance - 1) / chargeDistance) + 1) * ADDITIONAL_CHARGE_RATE);
    }
}

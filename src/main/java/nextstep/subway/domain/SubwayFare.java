package nextstep.subway.domain;

import nextstep.member.domain.Member;

public class SubwayFare {
    private static final int BASIC_FARE = 1250;
    private static final int BASIC_DISTANCE = 10;
    private static final int CHARGE_DISTANCE_FIVE = 5;
    private static final int CHARGE_DISTANCE_EIGHT = 8;
    private static final int ADDITIONAL_CHARGE_RATE = 100;
    private static final int DISTANCE_FIFTY = 50;
    private static final int DEDUCTION_FEE = 350;

    private SubwayFare() {
    }

    private static int calculateOverFare(int distance, int chargeDistance) {
        return (int) ((Math.ceil((distance - 1) / chargeDistance) + 1) * ADDITIONAL_CHARGE_RATE);
    }

    public static int calculateDistanceFare(int distance) {
        validateDistance(distance);

        int fare = 0;

        if (distance > BASIC_DISTANCE && distance <= DISTANCE_FIFTY) {
            fare += calculateOverFare(distance - BASIC_DISTANCE, CHARGE_DISTANCE_FIVE);
        }

        if (distance > DISTANCE_FIFTY) {
            int distanceOverFifty = distance - DISTANCE_FIFTY;
            fare += calculateOverFare(distance - BASIC_DISTANCE - distanceOverFifty, CHARGE_DISTANCE_FIVE);
            fare += calculateOverFare(distanceOverFifty, CHARGE_DISTANCE_EIGHT);
        }

        return fare;
    }

    private static void validateDistance(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("거리가 올바르지 않습니다.");
        }
    }

    public static int calculateFare(Path path, Member member) {
        int totalFare = BASIC_FARE;

        totalFare += calculateDistanceFare(path.extractDistance());
        totalFare += calculateLineFare(path.getSections());
        totalFare = calculateAgeFare(member, totalFare);

        return totalFare;
    }

    public static int calculateLineFare(Sections sections) {
        return sections.getSections().stream()
                .map(Section::getLine)
                .map(Line::getSurcharge)
                .reduce(Integer::max)
                .orElse(0);
    }

    public static int calculateAgeFare(Member member, int fare) {
        int resultAmount = fare;
        double discountAmount = 0;

        if (AgeDiscount.isDiscount(member.getAge())) {
            resultAmount -= DEDUCTION_FEE;
            AgeDiscount ageDiscount = AgeDiscount.getByAge(member.getAge());
            discountAmount = resultAmount * ageDiscount.getDiscountRate();
        }

        return (int) (resultAmount - discountAmount);
    }
}

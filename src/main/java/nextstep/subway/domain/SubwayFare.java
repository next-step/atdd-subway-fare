package nextstep.subway.domain;

import nextstep.member.domain.Member;

public class SubwayFare {
    private static final int BASIC_FARE = 1250;
    private static final int BASIC_DISTANCE = 10;
    private static final int CHARGE_DISTANCE_FIVE = 5;
    private static final int CHARGE_DISTANCE_EIGHT = 8;
    private static final int ADDITIONAL_CHARGE_RATE = 100;

    private SubwayFare() {
    }

    private static int calculateOverFare(int distance, int chargeDistance) {
        return (int) ((Math.ceil((distance - 1) / chargeDistance) + 1) * ADDITIONAL_CHARGE_RATE);
    }

    public static int calculateDistanceFare(int distance) {
        validateDistance(distance);

        int fare = BASIC_FARE;

        if (distance > BASIC_DISTANCE && distance <= 50) {
            fare += calculateOverFare(distance - BASIC_DISTANCE, CHARGE_DISTANCE_FIVE);
        }

        if (distance > 50) {
            fare += (calculateOverFare(distance - BASIC_DISTANCE, CHARGE_DISTANCE_FIVE) +
                    calculateOverFare(distance - 50, CHARGE_DISTANCE_EIGHT));
        }

        return fare;
    }

    private static void validateDistance(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("거리가 올바르지 않습니다.");
        }
    }

    public static int calculateFare(Path path, Member member) {
        int distanceFare = calculateDistanceFare(path.extractDistance());
        int lineSurcharge = calculateLineFare(path.getSections());

        return calculateAgeFare(member, distanceFare + lineSurcharge);
    }

    public static int calculateLineFare(Sections sections) {
        return sections.getSections().stream()
                .map(Section::getLine)
                .map(Line::getSurcharge)
                .reduce(Integer::max)
                .orElse(0);
    }

    public static int calculateAgeFare(Member member, int fare) {
        double discountRate = 0.0;
        int deductionFee = 350;
        int resultAmount = fare;

        if (member.getAge() >= 13 && member.getAge() < 19) {
            resultAmount -= deductionFee;
            discountRate = 0.2;
        } else if (member.getAge() >= 6 && member.getAge() < 13) {
            resultAmount -= deductionFee;
            discountRate = 0.5;
        }

        double discountAmount = resultAmount * discountRate;
        return (int) (resultAmount - discountAmount);
    }
}

package nextstep.subway.util.fare;

import nextstep.subway.domain.Path;

public class FareCalculator {
    public static int calculate(Path path, int age) {
        int distanceFare = calculateDistanceFare(path);
        int lineSurcharge = calculateLineSurcharge(path);
        int fare = distanceFare + lineSurcharge;

        int discount = calculateAgeDiscount(fare, age);

        return fare - discount;
    }

    private static int calculateDistanceFare(Path path) {
        return DistanceFarePolicy.calculate(path.extractDistance());
    }


    private static int calculateLineSurcharge(Path path) {
        return LineSurchargePolicy.calculate(path.getPassingLines());
    }

    private static int calculateAgeDiscount(int fare, int age) {
        return AgeDiscountPolicy.calculate(fare, age);
    }
}

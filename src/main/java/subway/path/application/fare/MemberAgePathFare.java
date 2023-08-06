package subway.path.application.fare;

import subway.member.domain.Member;
import subway.path.application.dto.PathFareCalculationInfo;

public class MemberAgePathFare extends PathFareChain {

    @Override
    public PathFareCalculationInfo calculateFare(PathFareCalculationInfo calcInfo) {
        if (calcInfo.getMemberAge() < 1) {
            return calcInfo;
        }

        final long memberAge = calcInfo.getMemberAge();
        double discountRate = getDiscountRateByAge(memberAge);

        long memberDiscountFare = calculateDiscountedFare(calcInfo.getFare(), discountRate);
        PathFareCalculationInfo calcInfoResponse = calcInfo.withUpdatedFare(memberDiscountFare);

        return super.nextCalculateFare(calcInfoResponse);
    }

    private double getDiscountRateByAge(long age) {
        if (age >= 6 && age < 13) {
            return 0.5;
        }
        if (age >= 13 && age < 19) {
            return 0.8;
        }
        return 1.0;
    }

    private long calculateDiscountedFare(long fare, double discountRate) {
        if (discountRate == 1.0) {
            return fare;
        } else {
            long deduction = fare - 350;
            return (long) (deduction * discountRate);
        }
    }
}

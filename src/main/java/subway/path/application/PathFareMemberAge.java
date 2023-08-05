package subway.path.application;

import subway.member.domain.Member;
import subway.path.application.dto.PathFareCalculationInfo;

public class PathFareMemberAge extends PathFareChain{

    @Override
    public PathFareCalculationInfo calculateFare(PathFareCalculationInfo calcInfo) {
        if (calcInfo.getMember() == null) {
            return calcInfo;
        }

        Member targetMember = calcInfo.getMember();
        long memberAge = targetMember.getAge();
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
            // 19세 초과인 경우, 아무런 공제도 적용되지 않습니다.
            return fare;
        } else {
            long deduction = fare - 350;
            return (long) (deduction * discountRate);
        }
    }
}

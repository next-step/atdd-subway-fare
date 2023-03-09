package nextstep.subway.applicaion;

import nextstep.member.domain.AuthenticatedUser;
import nextstep.member.domain.LoginMember;
import nextstep.subway.domain.AgeFarePolicy;
import nextstep.subway.domain.DistanceFarePolicy;
import nextstep.subway.domain.LineSurchargePolicy;
import nextstep.subway.domain.Path;

public class FareCalculator {

    /**
     * 요금 정책
     *
     * 1. 추가 거리별 추가 운임
     * 2. 이용 노선 중 가장 큰 추가 운임
     * 3. 최종 금액에서 나이별 할인
     */
    public int getTotalFare(AuthenticatedUser user, Path path) {
        int totalFare = 0;

        // 1. 추가 거리별 추가 운임
        totalFare += getCalculatedDistanceFare(path);

        // 2. 이용 노선 중 가장 큰 추가 운임
        totalFare += getCalculatedHighestSurcharge(path);

        // 3. 최종 금액에서 나이별 할인
        totalFare = getCalculatedAgeFare(totalFare, user);

        return totalFare;
    }

    private int getCalculatedDistanceFare(Path path) {
        int distance = path.extractDistance();
        return DistanceFarePolicy.calculate(distance);
    }

    private int getCalculatedHighestSurcharge(Path path) {
        return LineSurchargePolicy.calculate(path);
    }

    private int getCalculatedAgeFare(int fare, AuthenticatedUser user) {
        int age = -1;
        if (user instanceof LoginMember) {
            LoginMember loginMember = (LoginMember) user;
            age = loginMember.getAge();
        }
        return AgeFarePolicy.calculate(fare, age);
    }
}

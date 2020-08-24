package nextstep.subway.maps.map.dto;

import nextstep.subway.auth.application.UserDetails;
import nextstep.subway.fare.*;
import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.map.application.FareCalculator;
import nextstep.subway.maps.map.domain.LineStationEdge;
import nextstep.subway.maps.map.domain.SubwayPath;
import nextstep.subway.members.member.domain.LoginMember;

public class PathResponseAssembler {

    private static final int CHILD_MINIMUM_AGE = 6;
    private static final int CHILD_MAXIMUM_AGE = 13;
    private static final int YOUTH_MINIMUM_AGE = 13;
    private static final int YOUTH_MAXIMUM_AGE = 20;

    private PathResponseAssembler() {
    }

    public static PathResponse assemble(StationInfoDto stationInfoDto, UserDetails userDetails) {
        PolicyCalculator policyCalculator = getFareCalculator(userDetails);

        int distanceFare = new FareCalculator().calculate(stationInfoDto.getDistance());
        int totalFare = policyCalculator.calculate(distanceFare, stationInfoDto.getMaxExtraFare());

        return new PathResponse(stationInfoDto, totalFare);
    }

    private static PolicyCalculator getFareCalculator(UserDetails userDetails) {
        if (isLoginMember(userDetails)) {
            LoginMember loginMember = (LoginMember) userDetails;

            if (loginMember.getAge() >= CHILD_MINIMUM_AGE && loginMember.getAge() < CHILD_MAXIMUM_AGE) {
                return new PolicyCalculator(new ChildPolicy(new LineExtraFarePolicy()));
            }

            if (loginMember.getAge() >= YOUTH_MINIMUM_AGE && loginMember.getAge() < YOUTH_MAXIMUM_AGE) {
                return new PolicyCalculator(new YouthPolicy(new LineExtraFarePolicy()));
            }
        }
        return new PolicyCalculator(new NoneLoginPolicy(new LineExtraFarePolicy()));
    }

    private static boolean isLoginMember(UserDetails userDetails) {
        return userDetails instanceof LoginMember;
    }

}

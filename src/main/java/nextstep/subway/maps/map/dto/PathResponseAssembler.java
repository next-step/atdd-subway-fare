package nextstep.subway.maps.map.dto;

import nextstep.subway.auth.application.UserDetails;
import nextstep.subway.fare.*;
import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.map.application.FareCalculator;
import nextstep.subway.maps.map.domain.LineStationEdge;
import nextstep.subway.maps.map.domain.SubwayPath;
import nextstep.subway.members.member.domain.LoginMember;

public class PathResponseAssembler {

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

            if (loginMember.getAge() >= 6 && loginMember.getAge() < 13) {
                return new PolicyCalculator(new ChildPolicy(new LineExtraFarePolicy()));
            }

            if (loginMember.getAge() >= 13 && loginMember.getAge() < 20) {
                return new PolicyCalculator(new YouthPolicy(new LineExtraFarePolicy()));
            }
        }
        return new PolicyCalculator(new NoneLoginPolicy(new LineExtraFarePolicy()));
    }

    private static boolean isLoginMember(UserDetails userDetails) {
        return userDetails instanceof LoginMember;
    }

}

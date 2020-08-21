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

        int fare = new FareCalculator().calculate(stationInfoDto.getDistance());
        PolicyCalculator fareCalculator;

        if (isLoginMember(userDetails)) {
            LoginMember loginMember = (LoginMember) userDetails;

            if (loginMember.getAge() >= 6 && loginMember.getAge() < 13) {
                fareCalculator = new PolicyCalculator(new ChildPolicy(new LineExtraFarePolicy()));
                fare = fareCalculator.calculate(fare, stationInfoDto.getMaxExtraFare());
            }

            if (loginMember.getAge() >= 13 && loginMember.getAge() < 20) {
                fareCalculator = new PolicyCalculator(new YouthPolicy(new LineExtraFarePolicy()));
                fare = fareCalculator.calculate(fare, stationInfoDto.getMaxExtraFare());
            }
        } else {
            fareCalculator = new PolicyCalculator(new NoneLoginPolicy(new LineExtraFarePolicy()));
            fare = fareCalculator.calculate(fare, stationInfoDto.getMaxExtraFare());
        }

        return new PathResponse(stationInfoDto, fare);
    }

    private static boolean isLoginMember(UserDetails userDetails) {
        return userDetails instanceof LoginMember;
    }

}

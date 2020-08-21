package nextstep.subway.maps.map.dto;

import nextstep.subway.auth.application.UserDetails;
import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.map.application.FareCalculator;
import nextstep.subway.maps.map.domain.LineStationEdge;
import nextstep.subway.maps.map.domain.SubwayPath;
import nextstep.subway.members.member.domain.LoginMember;

public class PathResponseAssembler {


    public static PathResponse assemble(StationInfoDto stationInfoDto, UserDetails userDetails) {

        int fare = new FareCalculator().calculate(stationInfoDto.getDistance(), stationInfoDto.getMaxExtraFare());

        if (isLoginMember(userDetails)) {
            fare = ((LoginMember) userDetails).discountFare(fare);
        }

        return new PathResponse(stationInfoDto.getStationResponses(), stationInfoDto.getDuration(), stationInfoDto.getDistance(), fare);
    }

    private static boolean isLoginMember(UserDetails userDetails) {
        return userDetails instanceof LoginMember;
    }

    private static Integer getMaxExtraFare(SubwayPath subwayPath) {
        return subwayPath.getLineStationEdges().stream()
                .map(LineStationEdge::getLine)
                .map(Line::getExtraFare)
                .max(Integer::compareTo)
                .orElseThrow(IllegalArgumentException::new);
    }
}

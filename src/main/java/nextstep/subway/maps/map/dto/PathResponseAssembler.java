package nextstep.subway.maps.map.dto;

import nextstep.subway.auth.application.UserDetails;
import nextstep.subway.auth.domain.EmptyMember;
import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.map.application.FareCalculator;
import nextstep.subway.maps.map.domain.SubwayPath;
import nextstep.subway.maps.station.domain.Station;
import nextstep.subway.maps.station.dto.StationResponse;
import nextstep.subway.members.member.domain.LoginMember;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PathResponseAssembler {
    public static PathResponse assemble(SubwayPath subwayPath, Map<Long, Station> stations, LoginMember loginMember) {
        List<StationResponse> stationResponses = subwayPath.extractStationId().stream()
                .map(it -> StationResponse.of(stations.get(it)))
                .collect(Collectors.toList());

        // 가장 큰 추가요금으로 정산함.
        Integer maxExtraFare = getMaxExtraFare(subwayPath);

        int distance = subwayPath.calculateDistance();
        int fare = new FareCalculator().calculate(distance, maxExtraFare);

        if (isLoginMember(loginMember)) {
            fare = loginMember.discountFare(fare);
        }

        return new PathResponse(stationResponses, subwayPath.calculateDuration(), distance, fare);
    }

    private static boolean isLoginMember(LoginMember loginMember) {
        return !EmptyMember.class.isInstance(loginMember);
    }

    private static Integer getMaxExtraFare(SubwayPath subwayPath) {
        return subwayPath.getLineStationEdges().stream()
                .map(it -> it.getLine())
                .map(it -> it.getExtraFare())
                .max(Integer::compareTo)
                .get();
    }
}

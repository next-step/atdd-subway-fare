package nextstep.core.subway.path.application;

import nextstep.core.auth.domain.LoginMember;
import nextstep.core.member.application.MemberService;
import nextstep.core.subway.line.application.LineService;
import nextstep.core.subway.path.application.dto.PathFinderResponse;
import nextstep.core.subway.path.application.dto.PathRequest;
import nextstep.core.subway.path.domain.PathType;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private final LineService lineService;

    private final PathFinder pathFinder;

    private final MemberService memberService;

    public PathService(LineService lineService, PathFinder pathFinder, MemberService memberService) {
        this.lineService = lineService;
        this.pathFinder = pathFinder;
        this.memberService = memberService;
    }

    public PathFinderResponse findOptimalPath(PathRequest pathRequest, LoginMember loginMember) {
        validatePathRequest(pathRequest);

        if(loginMember == null) {
            return pathFinder.findOptimalPath(
                    lineService.findAllLines(),
                    lineService.findStation(pathRequest.getDepartureStationId()),
                    lineService.findStation(pathRequest.getArrivalStationId()),
                    PathType.findType(pathRequest.getPathFinderType()));
        }

        return pathFinder.findOptimalPath(
                lineService.findAllLines(),
                lineService.findStation(pathRequest.getDepartureStationId()),
                lineService.findStation(pathRequest.getArrivalStationId()),
                PathType.findType(pathRequest.getPathFinderType()),
                memberService.findMe(loginMember).getAge());

    }

    public boolean isValidPath(PathRequest pathRequest) {
        validatePathRequest(pathRequest);

        return pathFinder.existPathBetweenStations(
                lineService.findAllLines(),
                lineService.findStation(pathRequest.getDepartureStationId()),
                lineService.findStation(pathRequest.getArrivalStationId()));
    }

    private void validatePathRequest(PathRequest pathRequest) {
        if (areStationsSame(pathRequest)) {
            throw new IllegalArgumentException("출발역과 도착역이 동일할 수 없습니다.");
        }
    }

    private boolean areStationsSame(PathRequest pathRequest) {
        return pathRequest.getDepartureStationId().equals(pathRequest.getArrivalStationId());
    }
}

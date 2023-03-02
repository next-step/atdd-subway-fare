package nextstep.subway.applicaion;

import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.LoginMember;
import nextstep.subway.applicaion.dto.PathRequest;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {

    private final LineService lineService;
    private final StationService stationService;
    private final MemberService memberService;

    public PathService(LineService lineService, StationService stationService, MemberService memberService) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.memberService = memberService;
    }

    public PathResponse findPath(PathRequest pathRequest) {
        Path path = getPath(pathRequest);
        path.calcFareForNotMember();

        return PathResponse.of(path);
    }

    public PathResponse findPathWithMember(PathRequest pathRequest, LoginMember loginMember) {
        MemberResponse member = memberService.findMember(loginMember.getId());
        Path path = getPath(pathRequest);
        path.calcFareForMember(member);
        return PathResponse.of(path);
    }

    private Path getPath(PathRequest pathRequest) {
        Station upStation = stationService.findById(pathRequest.getSource());
        Station downStation = stationService.findById(pathRequest.getTarget());
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        return subwayMap.findPath(upStation, downStation, pathRequest.getType());
    }

}

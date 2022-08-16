package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.dto.PathRequest;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Fare;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import support.auth.userdetails.User;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PathService {
    private final LineService lineService;
    private final StationService stationService;
    private final MemberService memberService;

    public PathResponse findPath(PathRequest pathRequest, User loginUser) {
        Member member = memberService.findMember(loginUser.getUsername());
        Station upStation = stationService.findById(pathRequest.getSource());
        Station downStation = stationService.findById(pathRequest.getTarget());
        List<Line> lines = lineService.findLines();
        PathType type = PathType.of(pathRequest.getType());
        
        SubwayMap subwayMap = new SubwayMap(lines, type);
        Path path = subwayMap.findPath(upStation, downStation);
        Fare fare = path.extractFare(member);

        return PathResponse.of(path, fare);
    }
}

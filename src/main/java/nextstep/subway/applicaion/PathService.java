package nextstep.subway.applicaion;

import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.FindPathType;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import nextstep.subway.domain.fare.FareChain;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private LineService lineService;
    private StationService stationService;
    private FareChain fareChain;
    private MemberService memberService;

    public PathService(LineService lineService, StationService stationService, FareChain fareChain, MemberService memberService) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.fareChain = fareChain;
        this.memberService = memberService;
    }

    public PathResponse findPath(Long source, Long target, FindPathType type) {
        Path path = getPath(source, target, type);
        return PathResponse.of(path, fareChain, null);
    }

    public PathResponse findPath(Long source, Long target, FindPathType type, String userName) {
        Path path = getPath(source, target, type);
        MemberResponse memberResponse = memberService.findMemberByEmail(userName);
        return PathResponse.of(path, fareChain, memberResponse.getAge());
    }

    private Path getPath(Long source, Long target, FindPathType type) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        return subwayMap.findPath(upStation, downStation, type);
    }

}

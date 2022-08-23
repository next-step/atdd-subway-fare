package nextstep.subway.applicaion;

import lombok.AllArgsConstructor;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.dto.PathDto;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.service.SubwayMap;
import nextstep.subway.domain.service.decorator.concrete.SubwayFareCalculator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PathService {
    private final LineService lineService;
    private final StationService stationService;
    private final MemberService memberService;
    private final SubwayFareCalculator subwayFareCalculator;


    public PathResponse findPath(String username, PathDto pathDto) {
        Station upStation = stationService.findById(pathDto.getSource());
        Station downStation = stationService.findById(pathDto.getTarget());

        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPath(upStation, downStation, pathDto.getType());

        return PathResponse.of(path, calculateFare(username, path));
    }

    private Integer calculateFare(String username, Path path) {
        Member findMember = memberService.findMemberByUsername(username);
        return subwayFareCalculator.execute(findMember, path);
    }
}

package nextstep.subway.applicaion;

import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {

    private LineService lineService;
    private StationService stationService;

    private MemberRepository memberRepository;

    public PathService(LineService lineService, StationService stationService,
            MemberRepository memberRepository) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.memberRepository = memberRepository;
    }

    public PathResponse findPath(Integer age, Long source, Long target, String type) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPath(upStation, downStation, PathType.valueOf(type));

        SubwayFare subwayFare = new SubwayFare(SubwayFarePolicyType.byAge(age));
        int totalFare = subwayFare.calculateFare(path.extractExtraCharge(), path.getShortDistance());

        return PathResponse.of(path, totalFare);
    }

}

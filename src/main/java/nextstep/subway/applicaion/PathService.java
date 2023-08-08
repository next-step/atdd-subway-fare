package nextstep.subway.applicaion;

import nextstep.auth.principal.UserPrincipal;
import nextstep.member.application.MemberService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.DiscountFarePolicy;
import nextstep.subway.domain.DistanceFarePolicy;
import nextstep.subway.domain.ExtraChargeFarePolicy;
import nextstep.subway.domain.Fare;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
public class PathService {
    private LineService lineService;
    private StationService stationService;
    private MemberService memberService;

    public PathService(LineService lineService, StationService stationService, MemberService memberService) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.memberService = memberService;
    }

    public PathResponse findPath(Long source, Long target) {
        Path path = findPath(source, target, SubwayMap::of);
        Fare fare = getFare(path);
        return PathResponse.of(path, fare.get());
    }

    public PathResponse findPath(Long source, Long target, PathType pathType) {
        Path path = findPath(source, target, (lines) -> SubwayMap.of(lines, pathType));
        Fare fare = getFare(path);
        return PathResponse.of(path, fare.get());
    }

    public PathResponse findPath(UserPrincipal userPrincipal, Long source, Long target, PathType pathType) {
        Path path = findPath(source, target, (lines) -> SubwayMap.of(lines, pathType));
        int age = memberService.findMemberByEmail(userPrincipal.getUsername()).getAge();
        Fare fare = getFare(path, age);
        return PathResponse.of(path, fare.get());
    }

    private Path findPath(Long source, Long target, Function<List<Line>, SubwayMap> function) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = function.apply(lines);
        return subwayMap.findPath(upStation, downStation);
    }

    private Fare getFare(Path path) {
        Fare fare = Fare.of(DistanceFarePolicy.of(path.extractDistance()));
        fare.add(ExtraChargeFarePolicy.of(path.getSections().getMaxExtraCharge()));
        return fare;
    }

    private Fare getFare(Path path, int age) {
        Fare fare = getFare(path);
        fare.add(DiscountFarePolicy.of(age));
        return fare;
    }
}

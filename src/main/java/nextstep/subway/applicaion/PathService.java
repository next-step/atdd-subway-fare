package nextstep.subway.applicaion;

import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.discount.DiscountCalculator;
import nextstep.subway.domain.fare.AdditionalFarePolicy;
import nextstep.subway.domain.fare.BasicFarePolicy;
import nextstep.subway.domain.path.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import nextstep.subway.domain.path.PathStrategy;
import org.springframework.stereotype.Service;
import support.auth.userdetails.User;

import javax.naming.OperationNotSupportedException;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Service
public class PathService {
    private LineService lineService;
    private StationService stationService;

    private MemberService memberService;

    private Map<String, PathStrategy> pathStrategyMap;

    public PathService(LineService lineService, StationService stationService, MemberService memberService, Map<String, PathStrategy> pathStrategyMap) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.memberService = memberService;
        this.pathStrategyMap = pathStrategyMap;
    }

    public PathResponse findPath(User user, Long source, Long target, String type) {

        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();

        if(!pathStrategyMap.containsKey(type)) {
            throw new IllegalArgumentException();
        }

        SubwayMap subwayMap = new SubwayMap(lines, pathStrategyMap.get(type));
        Path path = subwayMap.findPath(upStation, downStation);
        path.addAllFarePolicy(List.of(new BasicFarePolicy(), new AdditionalFarePolicy()));
        long fare = path.calculateFare();

        if(user.getUsername() != null) {
            Member member = memberService.findByEmail(user.getUsername());
            fare = DiscountCalculator.applyToDiscountFare(DiscountCalculator.DiscountPolicy.get(member), fare);
        }

        return PathResponse.of(path, fare);
    }
}

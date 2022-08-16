package nextstep.subway.applicaion;

import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.discount.DiscountCalculator;
import nextstep.subway.domain.fare.AdditionalFarePolicy;
import nextstep.subway.domain.fare.BasicFarePolicy;
import nextstep.subway.domain.path.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import nextstep.subway.domain.path.PathStrategies;
import org.springframework.stereotype.Service;
import support.auth.userdetails.User;

import java.util.List;
import java.util.Map;


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

    public PathResponse findPath(User user, Long source, Long target, String type) {

        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();

        SubwayMap subwayMap = new SubwayMap(lines, PathStrategies.find(type));
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

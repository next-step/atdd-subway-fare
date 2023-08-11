package nextstep.subway.application;

import java.util.List;
import nextstep.auth.principal.UserPrincipal;
import nextstep.member.application.MemberService;
import nextstep.subway.application.dto.PathResponse;
import nextstep.subway.domain.AgeBasedDiscountPolicy;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import org.springframework.stereotype.Service;

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

    public PathResponse findPath(Long source, Long target, String typeName, UserPrincipal userPrincipal) {
        int age = AgeBasedDiscountPolicy.NO_DISCOUNT.getMinAge();
        if (!userPrincipal.isAnonymous()) {
            String email = userPrincipal.getUsername();
            age = memberService.findMemberByEmail(email).getAge();
        }
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        Path path = SubwayMap.findPath(typeName, upStation, downStation, lines, age);
        return PathResponse.of(path);
    }

    public void findPath(Long source, Long target) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap.findPath(upStation, downStation, lines);
    }
}

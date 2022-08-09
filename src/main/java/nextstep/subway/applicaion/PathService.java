package nextstep.subway.applicaion;

import nextstep.member.application.MemberService;
import nextstep.subway.domain.AgeDiscountPolicy;
import nextstep.subway.domain.PathType;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import org.springframework.stereotype.Service;
import support.auth.userdetails.User;

import java.util.List;

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

    public PathResponse findPath(User user, Long source, Long target, PathType type) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        AgeDiscountPolicy ageDiscountPolicy = AgeDiscountPolicy.of(getCurrentUserAge(user.getUsername()));
        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPath(upStation, downStation, type, ageDiscountPolicy);

        return PathResponse.of(path);
    }

    private int getCurrentUserAge(String email) {
        return memberService.findMember(email).getAge();
    }


}

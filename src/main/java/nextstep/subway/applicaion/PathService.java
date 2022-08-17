package nextstep.subway.applicaion;

import nextstep.member.application.MemberService;
import nextstep.subway.domain.DiscountPolicy;
import nextstep.subway.domain.PathType;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap.SubwayMap;
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

    public PathResponse findPath(User user, Long source, Long target, PathType type, String time) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        DiscountPolicy discountPolicy = DiscountPolicy.of(getCurrentUserAge(user.getUsername()));
        SubwayMap subwayMap = type.getInstance(lines);
        Path path = subwayMap.findPath(upStation, downStation, discountPolicy, time);

        return PathResponse.of(path);
    }

    private int getCurrentUserAge(String email) {
        return memberService.findMember(email).getAge();
    }


}

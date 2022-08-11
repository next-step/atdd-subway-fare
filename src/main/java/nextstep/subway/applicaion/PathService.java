package nextstep.subway.applicaion;

import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import nextstep.subway.util.Adult;
import nextstep.subway.util.Age;
import nextstep.subway.util.AgeFactory;
import org.springframework.stereotype.Service;
import support.auth.userdetails.AnonymousUser;
import support.auth.userdetails.UserDetails;

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

    public PathResponse findPath(Long source, Long target, UserDetails user) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);

        Path path = subwayMap.findPath(upStation, downStation, findLoginMemberAge(user));

        return PathResponse.of(path);
    }

    private Age findLoginMemberAge(UserDetails user) {
        if (user instanceof AnonymousUser) {
            return new Adult();
        }

        String loginEmail = (String) user.getUsername();
        MemberResponse loginMember = memberService.findMember(loginEmail);

        return AgeFactory.findUsersAge(loginMember.getAge());
    }
}

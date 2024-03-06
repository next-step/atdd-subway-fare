package nextstep.subway.application;

import nextstep.auth.application.UserDetailService;
import nextstep.auth.application.UserDetails;
import nextstep.member.application.MemberService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.subway.application.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.path.PathFinder;
import nextstep.subway.domain.path.PathFinderFactory;
import nextstep.subway.domain.path.PathType;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.path.fee.AgeType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;
    private final MemberService memberService;

    public PathService(LineService lineService, StationService stationService, MemberService memberService) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.memberService = memberService;
    }

    @Transactional(readOnly = true)
    public PathResponse findPath(final Long source, final Long target, final PathType type, LoginMember loginMember) {
        UserDetails userDetails = memberService.findByEmail(loginMember.getEmail());

        final Station sourceStation = stationService.findStationById(source);
        final Station targetStation = stationService.findStationById(target);
        final List<Line> lines = lineService.findAllLine();

        PathFinder pathFinder = PathFinderFactory.create(type);

        final AgeType ageType = AgeType.of(userDetails);
        return pathFinder.findPath(lines, sourceStation, targetStation, ageType);
    }
}

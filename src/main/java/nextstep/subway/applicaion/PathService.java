package nextstep.subway.applicaion;

import static nextstep.member.domain.MemberAgePolicy.UNKNOWN;

import java.util.List;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.MemberAgePolicy;
import nextstep.subway.applicaion.dto.response.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.path.Path;
import nextstep.subway.domain.path.PathFinder;
import nextstep.subway.domain.path.PathFinderFactory;
import nextstep.subway.domain.path.PathType;
import org.springframework.stereotype.Service;

@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;
    private final MemberService memberService;

    public PathService(
            final LineService lineService,
            final StationService stationService,
            final MemberService memberService
    ) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.memberService = memberService;
    }

    public PathResponse findPath(
            final LoginMember loginMember,
            final Long sourceId,
            final Long targetId,
            final String type
    ) {
        Station sourceStation = stationService.findById(sourceId);
        Station targetStation = stationService.findById(targetId);
        List<Line> lines = lineService.findLines();

        PathFinder pathFinder = PathFinderFactory.drawMap(lines, PathType.valueOf(type));
        Path path = pathFinder.findPath(sourceStation, targetStation);

        if (loginMember == null) {
            return PathResponse.of(path, UNKNOWN);
        }

        MemberResponse member = memberService.findMember(loginMember.getId());
        return PathResponse.of(path, MemberAgePolicy.of(member.getAge()));
    }
}

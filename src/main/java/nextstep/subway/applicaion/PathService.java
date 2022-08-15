package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.pathfinder.PathFinder;
import nextstep.subway.domain.*;
import org.springframework.stereotype.Service;
import support.auth.userdetails.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PathService {
    private final LineService lineService;
    private final StationService stationService;
    private final List<PathFinder> pathFinders;
    private final MemberService memberService;
    private final DiscountService discountService;

    public PathResponse findPath(final String email, Long source, Long target, PathType type) {
        final PathFinder pathFinder = findPathFinder(type);

        Path path = pathFinder.findPath(
                lineService.findLines(),
                stationService.findById(source),
                stationService.findById(target));

        final Member member = memberService.findMemberByEmail(email)
                .orElse(new Member("", "", 0));

        return PathResponse.of(path, discountService.findDiscountPolicy(member));
    }

    private PathFinder findPathFinder(final PathType type) {
        return pathFinders.stream()
                .filter(v -> v.findPathFinder(type) != null)
                .findAny()
                .orElseThrow();
    }
}

package nextstep.subway.ui;

import java.time.LocalTime;
import java.util.Optional;
import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.member.domain.LoginMember;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.farepolicy.discountcondition.FareDiscountCondition;
import nextstep.subway.domain.farepolicy.discountcondition.KidsFareDiscountCondition;
import nextstep.subway.domain.map.graphfactory.OneFieldWeightGraphFactory;
import nextstep.subway.domain.map.pathsearch.FastestArrivalTimePathSearchStrategy;
import nextstep.subway.domain.map.pathsearch.OneFieldPathSearchStrategy;
import nextstep.subway.domain.map.pathsearch.PathSearchStrategy;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("paths")
@RestController
public class PathController {
    private static final PathSearchStrategy SEARCH_BY_DISTANCE = new OneFieldPathSearchStrategy(
        new OneFieldWeightGraphFactory(section -> (double) section.getDistance())
    );
    private static final PathSearchStrategy SEARCH_BY_DURATION = new OneFieldPathSearchStrategy(
        new OneFieldWeightGraphFactory(section -> (double) section.getDuration())
    );

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("distance")
    public ResponseEntity<PathResponse> findPathByDistance(@AuthenticationPrincipal Optional<LoginMember> loginMember,
                                                           @RequestParam Long source, @RequestParam Long target) {
        PathResponse pathResponse = pathService.findPath(
            source, target, fareDiscountPolicy(loginMember), SEARCH_BY_DISTANCE
        );
        return ResponseEntity.ok(pathResponse);
    }

    @GetMapping("duration")
    public ResponseEntity<PathResponse> findPathByDuration(@AuthenticationPrincipal Optional<LoginMember> loginMember,
        @RequestParam Long source, @RequestParam Long target) {
        PathResponse pathResponse = pathService.findPath(
            source, target, fareDiscountPolicy(loginMember), SEARCH_BY_DURATION
        );
        return ResponseEntity.ok(pathResponse);
    }

    @GetMapping("arrival-time")
    public ResponseEntity<PathResponse> findPathByArrivalTime(@AuthenticationPrincipal Optional<LoginMember> loginMember,
        @RequestParam Long source, @RequestParam Long target, @DateTimeFormat(iso = ISO.TIME) @RequestParam LocalTime time) {
        PathResponse pathResponse = pathService.findPath(
            source, target, fareDiscountPolicy(loginMember),
            new FastestArrivalTimePathSearchStrategy(time)
        );
        return ResponseEntity.ok(pathResponse);
    }

    private FareDiscountCondition fareDiscountPolicy(Optional<LoginMember> loginMember) {
        return new KidsFareDiscountCondition(
            loginMember.map(LoginMember::getAge).orElse(0)
        );
    }
}

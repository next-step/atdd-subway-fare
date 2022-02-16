package nextstep.subway.ui;

import java.util.Optional;

import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.member.domain.LoginMember;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.farepolicy.discountcondition.FareDiscountCondition;
import nextstep.subway.domain.farepolicy.discountcondition.KidsFareDiscountCondition;
import nextstep.subway.domain.map.OneFieldWeightSubwayMapGraphFactory;
import nextstep.subway.domain.map.SubwayMapGraphFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("paths")
@RestController
public class PathController {
    private static final SubwayMapGraphFactory SUBWAY_MAP_GRAPH_FACTORY_FOR_DISTANCE = new OneFieldWeightSubwayMapGraphFactory(
        section -> (double) section.getDistance()
    );
    private static final SubwayMapGraphFactory SUBWAY_MAP_GRAPH_FACTORY_FOR_DURATION = new OneFieldWeightSubwayMapGraphFactory(
        section -> (double) section.getDuration()
    );

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("distance")
    public ResponseEntity<PathResponse> findPathByDistance(@AuthenticationPrincipal Optional<LoginMember> loginMember,
                                                           @RequestParam Long source, @RequestParam Long target) {
        PathResponse pathResponse = pathService.findPath(
            source, target, SUBWAY_MAP_GRAPH_FACTORY_FOR_DISTANCE, fareDiscountPolicy(loginMember)
        );
        return ResponseEntity.ok(pathResponse);
    }

    @GetMapping("duration")
    public ResponseEntity<PathResponse> findPathByDuration(@AuthenticationPrincipal Optional<LoginMember> loginMember,
                                                           @RequestParam Long source, @RequestParam Long target) {
        PathResponse pathResponse = pathService.findPath(
            source, target, SUBWAY_MAP_GRAPH_FACTORY_FOR_DURATION, fareDiscountPolicy(loginMember)
        );
        return ResponseEntity.ok(pathResponse);
    }

    private FareDiscountCondition fareDiscountPolicy(Optional<LoginMember> loginMember) {
        return new KidsFareDiscountCondition(
            loginMember.map(LoginMember::getAge).orElse(0)
        );
    }
}

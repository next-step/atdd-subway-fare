package nextstep.subway.ui;

import java.util.Optional;

import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.member.domain.LoginMember;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.farepolicy.FareCalculator;
import nextstep.subway.domain.farepolicy.FarePolicy;
import nextstep.subway.domain.farepolicy.discount.FareDiscountPolicy;
import nextstep.subway.domain.farepolicy.discount.KidsFareDiscountPolicy;
import nextstep.subway.domain.map.OneFieldSubwayMapGraphFactory;
import nextstep.subway.domain.map.SubwayMapGraphFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("paths")
@RestController
public class PathController {
    private static final FareCalculator FARE_CALCULATOR = new FareCalculator();
    private static final SubwayMapGraphFactory SUBWAY_MAP_GRAPH_FACTORY_FOR_DISTANCE = new OneFieldSubwayMapGraphFactory(
        section -> (double) section.getDistance()
    );
    private static final SubwayMapGraphFactory SUBWAY_MAP_GRAPH_FACTORY_FOR_DURATION = new OneFieldSubwayMapGraphFactory(
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
            source, target, SUBWAY_MAP_GRAPH_FACTORY_FOR_DISTANCE,
            FARE_CALCULATOR, fareDiscountPolicy(loginMember)
        );
        return ResponseEntity.ok(pathResponse);
    }

    @GetMapping("duration")
    public ResponseEntity<PathResponse> findPathByDuration(@AuthenticationPrincipal Optional<LoginMember> loginMember,
                                                           @RequestParam Long source, @RequestParam Long target) {
        PathResponse pathResponse = pathService.findPath(
            source, target, SUBWAY_MAP_GRAPH_FACTORY_FOR_DURATION,
            FARE_CALCULATOR, fareDiscountPolicy(loginMember)
        );
        return ResponseEntity.ok(pathResponse);
    }

    private FareDiscountPolicy fareDiscountPolicy(Optional<LoginMember> loginMember) {
        return new KidsFareDiscountPolicy(
            loginMember.map(LoginMember::getAge).orElse(0)
        );
    }
}

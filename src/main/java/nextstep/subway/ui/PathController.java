package nextstep.subway.ui;

import java.time.LocalTime;
import java.util.Optional;
import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.member.domain.LoginMember;
import nextstep.subway.applicaion.PathFacade;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.farepolicy.discountcondition.FareDiscountCondition;
import nextstep.subway.domain.farepolicy.discountcondition.KidsFareDiscountCondition;
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
    private final PathFacade pathFacade;

    public PathController(PathFacade pathFacade) {
        this.pathFacade = pathFacade;
    }

    @GetMapping("distance")
    public ResponseEntity<PathResponse> findPathByDistance(
        @AuthenticationPrincipal Optional<LoginMember> loginMember, @RequestParam long source, @RequestParam long target) {
        PathResponse pathResponse = pathFacade.findPathByDistance(
            source, target, fareDiscountPolicy(loginMember)
        );
        return ResponseEntity.ok(pathResponse);
    }

    @GetMapping("duration")
    public ResponseEntity<PathResponse> findPathByDuration(
        @AuthenticationPrincipal Optional<LoginMember> loginMember, @RequestParam long source, @RequestParam long target) {
        PathResponse pathResponse = pathFacade.findPathByDuration(
            source, target, fareDiscountPolicy(loginMember)
        );
        return ResponseEntity.ok(pathResponse);
    }

    @GetMapping("arrival-time")
    public ResponseEntity<PathResponse> findPathByArrivalTime(
        @AuthenticationPrincipal Optional<LoginMember> loginMember, @RequestParam long source, @RequestParam long target,
        @DateTimeFormat(iso = ISO.TIME) @RequestParam LocalTime time) {
        PathResponse pathResponse = pathFacade.findPathByArrivalTime(
            source, target, fareDiscountPolicy(loginMember), time
        );
        return ResponseEntity.ok(pathResponse);
    }

    private FareDiscountCondition fareDiscountPolicy(Optional<LoginMember> loginMember) {
        return new KidsFareDiscountCondition(
            loginMember.map(LoginMember::getAge).orElse(0)
        );
    }
}

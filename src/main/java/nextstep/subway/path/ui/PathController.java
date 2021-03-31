package nextstep.subway.path.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.line.domain.PathType;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.path.application.FareService;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.FareRequest;
import nextstep.subway.path.dto.FareResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {
    private FareService fareService;

    public PathController(FareService fareService) {
        this.fareService = fareService;
    }

    @GetMapping("/paths")
    public ResponseEntity< FareResponse > findPath(@AuthenticationPrincipal LoginMember loginMember,
                                                   @RequestParam Long source,
                                                   @RequestParam Long target,
                                                   @RequestParam PathType type) {
        final FareRequest fareRequest = new FareRequest(source, target, type, loginMember.getAge());
        return ResponseEntity.ok(fareService.calculate(fareRequest));
    }
}

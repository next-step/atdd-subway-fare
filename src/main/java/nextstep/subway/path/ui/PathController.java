package nextstep.subway.path.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.line.domain.PathType;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.path.application.FareService;
import nextstep.subway.path.dto.FareRequest;
import nextstep.subway.path.dto.FareResponse;
import nextstep.subway.path.exception.InvalidFareAmountException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
    public ResponseEntity< FareResponse > findPath(@AuthenticationPrincipal(required = false) LoginMember loginMember,
                                                   @RequestParam Long source,
                                                   @RequestParam Long target,
                                                   @RequestParam PathType type) {
        final FareRequest fareRequest = new FareRequest(source, target, type);
        return ResponseEntity.ok(fareService.calculate(fareRequest, loginMember));
    }

    @ExceptionHandler({ InvalidFareAmountException.class })
    public void handleInvalidFareAmountException(InvalidFareAmountException e) {
        // 응답을 뭘로 해야할지?
    }
}

package nextstep.subway.domain.path;

import nextstep.auth.domain.Account;
import nextstep.auth.ui.AuthenticationPrincipal;
import nextstep.subway.domain.path.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {

    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findLinePath(@AuthenticationPrincipal(acceptGuestRequest = true) Account account,
                                                     @RequestParam Long source,
                                                     @RequestParam Long target,
                                                     @RequestParam String type) {
        return ResponseEntity.ok(pathService.findPath(source, target, type, account));
    }
}

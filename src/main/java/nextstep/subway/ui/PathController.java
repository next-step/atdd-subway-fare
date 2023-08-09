package nextstep.subway.ui;

import nextstep.auth.principal.AuthenticationPrincipal;
import nextstep.auth.principal.LoginUserPrincipal;
import nextstep.auth.principal.UserPrincipal;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.SubwayMapType;
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
    public ResponseEntity<PathResponse> findPath(@RequestParam Long source,
                                                 @RequestParam Long target,
                                                 @RequestParam SubwayMapType type,
                                                 @AuthenticationPrincipal LoginUserPrincipal userPrincipal) {
        return ResponseEntity.ok(pathService.findPath(source, target, type, userPrincipal));
    }
}

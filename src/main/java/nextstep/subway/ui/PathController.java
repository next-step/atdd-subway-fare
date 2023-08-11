package nextstep.subway.ui;

import nextstep.auth.principal.AuthenticationPrincipal;
import nextstep.auth.principal.UserPrincipal;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.PathType;
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
                                                 @RequestParam(value = "type", defaultValue = "DISTANCE") PathType pathType) {
        return ResponseEntity.ok(pathService.findPath(source, target, pathType));
    }

    @GetMapping(value = "/paths", headers = {"Authorization"})
    public ResponseEntity<PathResponse> findPathWithAuthentication(@RequestParam Long source,
                                                 @RequestParam Long target,
                                                 @RequestParam(value = "type", defaultValue = "DISTANCE") PathType pathType,
                                                 @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(pathService.findPath(userPrincipal, source, target, pathType));
    }
}

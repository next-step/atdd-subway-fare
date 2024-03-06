package nextstep.path.ui;

import nextstep.auth.PathAuthenticationPrincipal;
import nextstep.auth.application.UserDetail;
import nextstep.path.service.PathService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("paths")
    public ResponseEntity<PathsResponse> getPaths(
            @RequestParam int source,
            @RequestParam int target,
            @RequestParam PathType type,
            @PathAuthenticationPrincipal UserDetail userDetail) {
        return ResponseEntity.ok(pathService.searchPath(source, target, type, userDetail));
    }
}

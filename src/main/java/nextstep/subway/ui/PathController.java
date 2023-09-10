package nextstep.subway.ui;

import nextstep.auth.principal.AuthenticationPrincipal;
import nextstep.auth.principal.UserPrincipal;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.FindPathType;
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
    public ResponseEntity<PathResponse> findPath(@RequestParam Long source, @RequestParam Long target, @RequestParam FindPathType type) {
        return ResponseEntity.ok(pathService.findPath(source, target, type));
    }

    @GetMapping("/paths/me")
    public ResponseEntity<PathResponse> findPath(@RequestParam Long source, @RequestParam Long target, @RequestParam FindPathType type, @AuthenticationPrincipal
            UserPrincipal userPrincipal) {
        return ResponseEntity.ok(pathService.findPath(source, target, type,
                userPrincipal.getUsername()));
    }
}

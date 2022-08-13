package nextstep.subway.ui;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import support.auth.authorization.AuthenticationPrincipal;
import support.auth.userdetails.User;

@RestController
public class PathController {

    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(
        @AuthenticationPrincipal(isMember = false) User user, @RequestParam Long source, @RequestParam Long target) {
        return ResponseEntity.ok(pathService.findPath(source, target, user.getAge()));
    }

    @GetMapping("/paths/time")
    public ResponseEntity<PathResponse> findMinimumTimePath(
        @AuthenticationPrincipal(isMember = false) User user, @RequestParam Long source, @RequestParam Long target) {
        return ResponseEntity.ok(pathService.findMinimumTimePath(source, target, user.getAge()));
    }

}

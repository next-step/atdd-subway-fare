package nextstep.subway.ui;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.path.PathType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import support.auth.authorization.AuthenticationPrincipal;
import support.auth.userdetails.User;

@RestController
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(@RequestParam Long source, @RequestParam Long target, @RequestParam PathType pathType, @AuthenticationPrincipal(required = false) User user) {
        return ResponseEntity.ok(pathService.findPath(source, target, pathType, user.getAge()));
    }
}

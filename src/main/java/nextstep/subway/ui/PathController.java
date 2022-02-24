package nextstep.subway.ui;

import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.member.domain.LoginMember;
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
    public ResponseEntity<PathResponse> findPath(@AuthenticationPrincipal(required = false) LoginMember loginMember,
                                                 @RequestParam Long source,
                                                 @RequestParam Long target,
                                                 @RequestParam PathType pathType) {
        return ResponseEntity.ok(pathService.findPath(source, target, pathType, loginMember.getAge()));
    }
}

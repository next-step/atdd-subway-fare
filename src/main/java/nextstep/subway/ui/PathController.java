package nextstep.subway.ui;

import nextstep.global.AuthenticationPrincipal;
import nextstep.member.domain.LoginMember;
import nextstep.subway.application.PathService;
import nextstep.subway.application.dto.PathResponse;
import nextstep.subway.domain.path.PathType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(@RequestParam Long source, @RequestParam Long target,
                                                 @RequestParam PathType type,
                                                 @AuthenticationPrincipal(required = false) LoginMember loginMember) {
        return ResponseEntity.ok(pathService.findPath(source, target, type, loginMember));
    }
}





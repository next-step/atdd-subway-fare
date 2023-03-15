package nextstep.subway.ui;

import nextstep.member.domain.AuthenticationPrincipal;
import nextstep.member.domain.LoginMember;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.PathType;
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
    public ResponseEntity<PathResponse> findPath(@AuthenticationPrincipal(required = false) final LoginMember member,
                                                 @RequestParam final Long source,
                                                 @RequestParam final Long target,
                                                 @RequestParam final PathType type) {
        return ResponseEntity.ok(pathService.findPath(member, source, target, type));
    }
}

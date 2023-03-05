package nextstep.subway.ui;

import nextstep.member.domain.AuthenticationPrincipal;
import nextstep.member.domain.AuthMember;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.PathSearchType;
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
    public ResponseEntity<PathResponse> findPath(@AuthenticationPrincipal(required = false) AuthMember authMember, @RequestParam Long source, @RequestParam Long target, @RequestParam(name = "type") PathSearchType pathSearchType) {
        return ResponseEntity.ok(pathService.findPath(authMember.getId(), source, target, pathSearchType));
    }
}

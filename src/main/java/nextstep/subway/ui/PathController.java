package nextstep.subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nextstep.member.domain.AuthenticationPrincipal;
import nextstep.member.domain.LoginMember;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.PathSearchType;

@RestController
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(
        @AuthenticationPrincipal LoginMember loginMember,
        @RequestParam Long source,
        @RequestParam Long target,
        @RequestParam(name = "type") PathSearchType pathSearchType
    ) {
        return ResponseEntity.ok(pathService.findPath(source, target, pathSearchType, loginMember.getAge()));
    }
}

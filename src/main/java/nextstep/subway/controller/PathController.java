package nextstep.subway.controller;

import nextstep.auth.ui.AuthenticationPrincipal;
import nextstep.auth.ui.LoginMember;
import nextstep.subway.controller.dto.PathResponse;
import nextstep.subway.service.PathService;
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

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPaths(
            @AuthenticationPrincipal LoginMember loginMember,
            @RequestParam(name = "source") Long sourceId,
            @RequestParam("target") Long targetId,
            @RequestParam("type") String pathType
    ) {
        PathResponse pathResponse = pathService.findPaths(loginMember, sourceId, targetId, pathType);
        return ResponseEntity.ok().body(pathResponse);
    }
}

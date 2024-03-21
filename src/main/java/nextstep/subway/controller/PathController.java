package nextstep.subway.controller;

import nextstep.auth.domain.UserDetail;
import nextstep.auth.ui.AuthenticationPrincipal;
import nextstep.subway.controller.dto.PathResponse;
import nextstep.subway.domain.PathType;
import nextstep.subway.service.PathService;
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

    @GetMapping(value = "/paths")
    public ResponseEntity<PathResponse> findPath(
            @RequestParam("source") Long source,
            @RequestParam("target") Long target,
            @RequestParam("type") PathType pathType,
            @AuthenticationPrincipal(required = false) UserDetail userDetail
    ) {
        return ResponseEntity.ok().body(pathService.findPath(source, target, pathType, userDetail.getAge()));
    }
}

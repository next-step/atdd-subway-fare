package nextstep.subway.controller;

import nextstep.subway.controller.dto.PathResponse;
import nextstep.subway.service.PathService;
import nextstep.subway.strategy.PathType;
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
            @RequestParam(name = "source") Long sourceId,
            @RequestParam("target") Long targetId,
            @RequestParam("type") PathType pathType
    ) {
        PathResponse pathResponse = pathService.findPaths(sourceId, targetId, pathType);
        return ResponseEntity.ok().body(pathResponse);
    }
}

package nextstep.subway.controller;

import nextstep.subway.controller.resonse.PathResponse;
import nextstep.subway.domain.enums.PathType;
import nextstep.subway.service.PathFindService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {
    private final PathFindService pathFindService;

    public PathController(PathFindService pathFindService) {
        this.pathFindService = pathFindService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(@RequestParam Long source, @RequestParam Long target, @RequestParam PathType type) {
        PathResponse shortestPathResponse = pathFindService.getPath(source, target, type);
        return ResponseEntity.ok(shortestPathResponse);
    }

}

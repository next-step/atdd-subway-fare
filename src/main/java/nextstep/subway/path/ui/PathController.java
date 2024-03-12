package nextstep.subway.path.ui;

import nextstep.subway.path.application.PathService;
import nextstep.subway.path.domain.PathType;
import nextstep.subway.path.application.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> shortestPath(@RequestParam("source") Long source, @RequestParam("target") Long target, @RequestParam("type") PathType type) {
        return ResponseEntity.ok().body(pathService.getShortestPath(source, target, type));
    }
}

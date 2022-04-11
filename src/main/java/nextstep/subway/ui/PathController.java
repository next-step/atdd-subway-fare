package nextstep.subway.ui;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
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

    // Param 을 만들어서 전략패턴을 도입할까 했는데 본 목적이 아니여서 안했습니다.
    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPathByShortestDistance(@RequestParam Long source,
                                                                   @RequestParam Long target) {
        return ResponseEntity.ok(pathService.findPathByShortestDistance(source, target));
    }

    @GetMapping("/paths/shortest-duration")
    public ResponseEntity<PathResponse> findPathByShortestDuration(@RequestParam Long source,
                                                                   @RequestParam Long target) {
        return ResponseEntity.ok(pathService.findPathByShortestDistance(source, target));
    }
}

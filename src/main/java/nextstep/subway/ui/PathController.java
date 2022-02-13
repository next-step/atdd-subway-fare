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

    @GetMapping("/paths/distance")
    public ResponseEntity<PathResponse> findPathByDistance(@RequestParam Long source, @RequestParam Long target) {
        return ResponseEntity.ok(pathService.findPathByDistance(source, target));
    }

    @GetMapping("/paths/duration")
    public ResponseEntity<PathResponse> findPathByDuration(@RequestParam Long source, @RequestParam Long target) {
        return ResponseEntity.ok(pathService.findPathByDuration(source, target));
    }
}

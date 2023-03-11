package nextstep.subway.ui;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathMinimumDistanceResponse;
import nextstep.subway.applicaion.dto.PathMinimumDurationResponse;
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

    @GetMapping("/paths/minimum-distance")
    public ResponseEntity<PathMinimumDistanceResponse> findPathOfMinimumDistance(@RequestParam Long source, @RequestParam Long target) {
        return ResponseEntity.ok(pathService.findPathOfMinimumDistance(source, target));
    }

    @GetMapping("/paths/minimum-duration")
    public ResponseEntity<PathMinimumDurationResponse> findPathOfMinimumDuration(@RequestParam Long source, @RequestParam Long target) {
        return ResponseEntity.ok(pathService.findPathOfMinimumDuration(source, target));
    }
}

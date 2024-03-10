package nextstep.core.subway.pathFinder.presentation;

import nextstep.core.subway.pathFinder.application.PathService;
import nextstep.core.subway.pathFinder.application.dto.PathFinderRequest;
import nextstep.core.subway.pathFinder.application.dto.PathFinderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathFinderController {

    public final PathService pathService;

    public PathFinderController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathFinderResponse> findOptimalPath(@RequestParam("source") Long departureStationId,
                                                              @RequestParam("target") Long arrivalStationId,
                                                              @RequestParam("type") String pathFinderType) {
        PathFinderRequest pathFinderRequest = new PathFinderRequest(departureStationId, arrivalStationId, pathFinderType);
        return ResponseEntity.ok(pathService.findOptimalPath(pathFinderRequest));
    }
}

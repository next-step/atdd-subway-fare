package nextstep.subway.ui;

import nextstep.subway.dto.PathResponse;
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
    ResponseEntity getShortestPath(@RequestParam("source") long sourceStationId, @RequestParam("target") long targetStationId) {
        PathResponse pathResponse = pathService.getShortestPath(sourceStationId, targetStationId);
        return ResponseEntity.ok(pathResponse);
    }
}

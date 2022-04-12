package nextstep.subway.ui;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.desginpattern.DirectWeightGraphFactory;
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

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPathByShortestDistance(@RequestParam Long source,
                                                                   @RequestParam Long target,
                                                                   @RequestParam(defaultValue = "distance") String condition) {
        return ResponseEntity.ok(pathService.findPathByShortestCondition(source, target, DirectWeightGraphFactory.factory(condition)));
    }
}

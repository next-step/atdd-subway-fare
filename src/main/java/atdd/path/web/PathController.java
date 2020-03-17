package atdd.path.web;

import atdd.path.application.GraphService;
import atdd.path.application.dto.MinTimePathAssembler;
import atdd.path.application.dto.PathResponseView;
import atdd.path.domain.Line;
import atdd.path.domain.MinTimePathLine;
import atdd.path.domain.Station;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/paths")
public class PathController {
    private GraphService graphService;

    public PathController(GraphService graphService) {
        this.graphService = graphService;
    }

    @GetMapping("/short-path")
    public ResponseEntity findPath(@RequestParam Long startId, @RequestParam Long endId) {
        return ResponseEntity.ok(new PathResponseView(startId, endId, graphService.findPath(startId, endId)));
    }

    @GetMapping("/min-time")
    public ResponseEntity findMinTimePath(@RequestParam Long startId, @RequestParam Long endId) {
        List<Station> stations = graphService.findMinTimePath(startId, endId);
        List<Line> lines = graphService.findAllLine();

        return ResponseEntity.ok(new MinTimePathAssembler(lines, stations, LocalDateTime.now(), MinTimePathLine.listOf(lines, stations)).assemble());
    }
}

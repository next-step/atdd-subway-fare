package atdd.path.web;

import atdd.path.application.GraphService;
import atdd.path.application.dto.MinTimePathResponseView;
import atdd.path.application.dto.PathResponseResource;
import atdd.path.application.dto.PathResponseView;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static atdd.Constant.PATH_BASE_URI;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(PATH_BASE_URI)
public class PathController {
    private GraphService graphService;

    public PathController(GraphService graphService) {
        this.graphService = graphService;
    }

    @GetMapping
    public ResponseEntity findPath(@RequestParam Long startId, @RequestParam Long endId) {
        MinTimePathResponseView minTimePath = graphService.findMinTimePath(startId, endId);
        return ResponseEntity
                .ok(minTimePath);
    }
}

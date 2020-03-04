package atdd.path.web;

import atdd.path.application.LineService;
import atdd.path.application.dto.CreateEdgeRequestView;
import atdd.path.application.dto.CreateLineRequestView;
import atdd.path.application.dto.LineResponseView;
import atdd.path.dao.LineDao;
import atdd.path.domain.Line;
import atdd.path.repository.LineRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {
    private LineDao lineDao;
    private LineRepository lineRepository;
    private LineService lineService;

    public LineController(LineDao lineDao, LineService lineService, LineRepository lineRepository) {
        this.lineDao = lineDao;
        this.lineService = lineService;
        this.lineRepository = lineRepository;
    }

    @PostMapping
    public ResponseEntity createLine(@RequestBody CreateLineRequestView view) {
        Line persistLine = lineDao.save(view.toLine());
        return ResponseEntity.created(URI.create("/lines/" + persistLine.getId())).body(LineResponseView.of(persistLine));
    }

    @GetMapping("{id}")
    public ResponseEntity retrieveLine(@PathVariable Long id) {
       return lineRepository.findById(id)
               .map(it -> ResponseEntity.ok().body(LineResponseView.of(it)))
               .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity showLine() {
        List<Line> persistLines = lineDao.findAll();
        return ResponseEntity.ok().body(LineResponseView.listOf(persistLines));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteLine(@PathVariable Long id) {
        lineDao.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/edges")
    public ResponseEntity createEdge(@PathVariable Long id, @RequestBody CreateEdgeRequestView view) {
        lineService.addEdge(id, view.getSourceId(), view.getTargetId(), view.getDistance(), view.getElapsedMinutes());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/edges")
    public ResponseEntity deleteStation(@PathVariable Long id, @RequestParam Long stationId) {
        lineService.deleteStation(id, stationId);
        return ResponseEntity.ok().build();
    }
}

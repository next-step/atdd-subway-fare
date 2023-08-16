package nextstep.line.ui;

import nextstep.line.application.LineService;
import nextstep.line.application.request.LineCreateRequest;
import nextstep.line.application.request.LineModifyRequest;
import nextstep.line.application.request.SectionAddRequest;
import nextstep.line.application.response.LineResponse;
import nextstep.line.application.response.ShortPathResponse;
import nextstep.line.domain.path.ShortPathType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineCreateRequest lineCreateRequest) {
        LineResponse lineResponse = lineService.saveLine(lineCreateRequest);
        return ResponseEntity.created(URI.create("/lines/" + lineResponse.getId())).body(lineResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modifyLine(@PathVariable Long id, @RequestBody LineModifyRequest lineModifyRequest) {
        lineService.modifyLine(id, lineModifyRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/sections")
    public ResponseEntity<Void> addSection(@PathVariable Long id, @RequestBody SectionAddRequest sectionAddRequest) {
        lineService.addSection(id, sectionAddRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findLines() {
        return ResponseEntity.ok().body(lineService.findLines());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> findLine(@PathVariable Long id) {
        return ResponseEntity.ok().body(lineService.findLine(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLine(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable Long id, @RequestParam Long stationId) {
        lineService.deleteSection(id, stationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/paths")
    public ResponseEntity<ShortPathResponse> findShortPath(@RequestParam Long startStationId,
                                                           @RequestParam Long endStationId,
                                                           @RequestParam ShortPathType type) {
        ShortPathResponse shortPath = lineService.findShortPath(type, startStationId, endStationId);
        return ResponseEntity.ok().body(shortPath);
    }

}
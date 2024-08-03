package nextstep.subway.line.ui;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import nextstep.subway.line.application.LineService2;
import nextstep.subway.line.application.dto.*;
import nextstep.subway.line.domain.Line2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lines")
@RequiredArgsConstructor
public class LineController {
  private final LineService2 lineService;

  @PostMapping
  public ResponseEntity<LineResponse2> createLine(@RequestBody LineRequest2 request) {
    Line2 line = lineService.saveLine(request);
    return ResponseEntity.created(URI.create("/lines/" + line.getId()))
        .body(LineResponse2.from(line));
  }

  @GetMapping
  public ResponseEntity<List<LineResponse2>> showLines() {
    List<Line2> lines = lineService.findAllLines();
    return ResponseEntity.ok().body(LineResponse2.listOf(lines));
  }

  @GetMapping("/{id}")
  public ResponseEntity<LineResponse2> showLine(@PathVariable Long id) {
    Line2 line = lineService.findLineById(id);
    return ResponseEntity.ok().body(LineResponse2.from(line));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Void> updateLine(
      @PathVariable Long id, @RequestBody UpdateLineRequest request) {
    lineService.updateLineById(id, request);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
    lineService.deleteLineById(id);
    return ResponseEntity.noContent().build();
  }
}

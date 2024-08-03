package nextstep.subway.line.ui;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.application.dto.*;
import nextstep.subway.line.domain.Line;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lines")
@RequiredArgsConstructor
public class LineController {
  private final LineService lineService;

  @PostMapping
  public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest request) {
    Line line = lineService.saveLine(request);
    return ResponseEntity.created(URI.create("/lines/" + line.getId()))
        .body(LineResponse.from(line));
  }

  @GetMapping
  public ResponseEntity<List<LineResponse>> showLines() {
    List<Line> lines = lineService.findAllLines();
    return ResponseEntity.ok().body(LineResponse.listOf(lines));
  }

  @GetMapping("/{id}")
  public ResponseEntity<LineResponse> showLine(@PathVariable Long id) {
    Line line = lineService.findLineById(id);
    return ResponseEntity.ok().body(LineResponse.from(line));
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

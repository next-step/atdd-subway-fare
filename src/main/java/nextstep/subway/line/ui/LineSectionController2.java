package nextstep.subway.line.ui;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import nextstep.subway.line.application.LineSectionService2;
import nextstep.subway.line.application.dto.LineResponse2;
import nextstep.subway.line.application.dto.LineSectionRequest2;
import nextstep.subway.line.domain.Line2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LineSectionController2 {
  private final LineSectionService2 lineSectionService;

  @PostMapping("/new/lines/{lineId}/sections")
  public ResponseEntity<LineResponse2> appendLineSection(
      @PathVariable Long lineId, @RequestBody LineSectionRequest2 request) {
    Line2 line = lineSectionService.appendLineSection(lineId, request);
    return ResponseEntity.created(URI.create("/lines/" + lineId + "/sections"))
        .body(LineResponse2.from(line));
  }

  @DeleteMapping("/new/lines/{lineId}/sections")
  public ResponseEntity<Void> removeLineSection(
      @PathVariable Long lineId, @RequestParam Long stationId) {
    lineSectionService.removeLineSection(lineId, stationId);
    return ResponseEntity.noContent().build();
  }
}

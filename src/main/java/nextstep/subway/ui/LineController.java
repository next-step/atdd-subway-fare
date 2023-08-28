package nextstep.subway.ui;

import nextstep.subway.dto.LineRequest;
import nextstep.subway.dto.LineResponse;
import nextstep.subway.dto.ModifyLineRequest;
import nextstep.subway.dto.SectionRequest;
import nextstep.subway.service.LineService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class LineController {
    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse response = lineService.save(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + response.getId())).body(response);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineResponse>> getLines() {
        List<LineResponse> responses = lineService.getLines();

        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineResponse> getLine(@PathVariable long id) {
        LineResponse response = lineService.findLineById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity substituteLine(@RequestBody ModifyLineRequest modifyLineRequest, @PathVariable long id) {
        lineService.updateLine(id, modifyLineRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity deleteLine(@PathVariable long id) {
        lineService.deleteLine(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/lines/{id}/sections")
    public ResponseEntity enrollSection(@PathVariable long id, @RequestBody SectionRequest sectionRequest) {
        lineService.addSection(id, sectionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/lines/{lineId}/sections")
    public ResponseEntity deleteSection(@PathVariable long lineId, @RequestParam(value = "stationId") long stationId) {
        lineService.deleteSection(lineId, stationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

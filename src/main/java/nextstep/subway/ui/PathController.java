package nextstep.subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;

@RestController
public class PathController {
	private PathService pathService;

	public PathController(PathService pathService) {
		this.pathService = pathService;
	}

	@GetMapping("/paths")
	public ResponseEntity<PathResponse> findPath(@RequestParam Long source, @RequestParam Long target) {
		return ResponseEntity.ok(pathService.findPath(source, target));
	}

	@GetMapping("/paths/duration")
	public ResponseEntity<PathResponse> findPathBaseOnDuration(@RequestParam Long source, @RequestParam Long target) {
		return null;
	}
}

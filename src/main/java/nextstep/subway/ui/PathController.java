package nextstep.subway.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.SearchType;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PathController {

	private final PathService pathService;

	@GetMapping("/paths")
	public ResponseEntity<PathResponse> findPath(
		@RequestParam Long source, @RequestParam Long target,
		@RequestParam(name = "type") SearchType type) {
		return ResponseEntity.ok(pathService.findPath(source, target, type));
	}
}

package nextstep.subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sun.istack.NotNull;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.path.PathBaseCode;

@RestController
public class PathController {
	private PathService pathService;

	public PathController(PathService pathService) {
		this.pathService = pathService;
	}

	@GetMapping("/paths")
	public ResponseEntity<PathResponse> findPath(@RequestParam Long source
		, @RequestParam Long target
		, @RequestParam @NotNull PathBaseCode pathBaseCode) {
		return ResponseEntity.ok(pathService.findPath(source, target, pathBaseCode));
	}

}

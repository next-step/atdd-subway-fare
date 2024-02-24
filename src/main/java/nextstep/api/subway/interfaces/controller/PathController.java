package nextstep.api.subway.interfaces.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nextstep.api.subway.application.PathFacade;
import nextstep.api.subway.common.type.PathType;
import nextstep.api.subway.interfaces.dto.response.PathResponse;

/**
 * @author : Rene Choi
 * @since : 2024/02/09
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/paths")
public class PathController {

	private final PathFacade pathFacade;


	@GetMapping
	public ResponseEntity<PathResponse> findShortestPath(@RequestParam Long source, @RequestParam Long target, @RequestParam(required = false) PathType type) {
		PathResponse pathResponse = pathFacade.findPath(source, target, type);
		return ResponseEntity.ok(pathResponse);
	}


}

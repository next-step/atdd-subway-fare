package nextstep.subway.ui.controller;

import nextstep.auth.AuthenticationPrincipal;
import nextstep.auth.domain.LoginMember;
import nextstep.subway.application.dto.PathResponse;
import nextstep.subway.application.service.PathService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {
	private final PathService pathService;

	public PathController(PathService pathService) {
		this.pathService = pathService;
	}

	@GetMapping("/paths")
	ResponseEntity<PathResponse> getPath(@AuthenticationPrincipal(required = false) LoginMember loginMember, @RequestParam Long source, @RequestParam Long target, @RequestParam PathType type) {
		if(source.equals(target)) {
			throw new IllegalArgumentException("출발역과 도착역이 같을 수 없습니다.");
		}

		if(ObjectUtils.isEmpty(loginMember.getId())) {
			return ResponseEntity.ok().body(pathService.getPath(source, target, type));
		}

		return ResponseEntity.ok().body(pathService.getPath(loginMember.getId(), source, target, type));
	}
}

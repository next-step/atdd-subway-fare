package nextstep.subway.path;

import lombok.RequiredArgsConstructor;
import nextstep.member.domain.AuthenticationPrincipal;
import nextstep.member.domain.LoginMember;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PathController {

	private final PathService pathService;

	@GetMapping("/paths")
	public ResponseEntity<PathResponse> findPath(
		@AuthenticationPrincipal LoginMember loginMember,
		@ModelAttribute PathRequest pathRequest) {
		return ResponseEntity.ok(pathService.findPath(loginMember, pathRequest));
	}
}

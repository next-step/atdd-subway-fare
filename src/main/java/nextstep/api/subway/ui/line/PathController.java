package nextstep.api.subway.ui.line;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nextstep.api.auth.aop.principal.AuthenticationPrincipal;
import nextstep.api.auth.aop.principal.UserPrincipal;
import nextstep.api.subway.applicaion.path.PathService;
import nextstep.api.subway.applicaion.path.dto.PathResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/paths")
public class PathController {
    private final PathService pathService;

    @GetMapping
    public ResponseEntity<PathResponse> showShortestPath(
            @AuthenticationPrincipal(required = false) final UserPrincipal userPrincipal,
            @RequestParam("source") final Long sourceId,
            @RequestParam("target") final Long targetId,
            @RequestParam("type") final String pathType
    ) {
        final var response = pathService.findShortestPath(userPrincipal, sourceId, targetId, pathType);
        return ResponseEntity.ok().body(response);
    }
}

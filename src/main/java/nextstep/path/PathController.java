package nextstep.path;

import lombok.RequiredArgsConstructor;
import nextstep.auth.principal.AuthenticationPrincipal;
import nextstep.auth.principal.LoginMember;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/paths")
public class PathController {
    private final PathService pathService;

    @GetMapping
    public ResponseEntity<PathResponse> showShortestPath(
            @RequestParam("source") Long sourceId,
            @RequestParam("target") Long targetId,
            @RequestParam("type") String type,
            @RequestParam(value = "time", required = false) String time,
            @AuthenticationPrincipal(required = false) LoginMember loginMember
            ) {
        return ResponseEntity.ok().body(pathService.showShortestPath(sourceId, targetId, type, time, loginMember));
    }
}

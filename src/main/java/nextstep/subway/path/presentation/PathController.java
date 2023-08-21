package nextstep.subway.path.presentation;

import nextstep.auth.principal.AuthenticationPrincipal;
import nextstep.auth.principal.UserPrincipal;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.UserDto;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<PathResponse> showPath(@AuthenticationPrincipal(required = false) UserPrincipal userPrincipal,
                                                 @RequestParam Long source,
                                                 @RequestParam Long target,
                                                 @RequestParam String type
    ) {
        return ResponseEntity.ok().body(pathService.searchPath(UserDto.of(userPrincipal), source, target, type));
    }
}

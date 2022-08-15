package nextstep.subway.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.PathType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import support.auth.authorization.AuthenticationPrincipal;
import support.auth.userdetails.User;

@RestController
@RequiredArgsConstructor
public class PathController {

    private final PathService pathService;

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(@AuthenticationPrincipal User user,
        @RequestParam Long source,
          @RequestParam Long target
        , @RequestParam PathType pathType) {
        return ResponseEntity.ok(pathService.findPath(source, target, pathType, user.getAge()));
    }
}

package nextstep.subway.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathRequest;
import nextstep.subway.applicaion.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import support.auth.authorization.AuthenticationPrincipal;
import support.auth.userdetails.User;

@RestController
@RequiredArgsConstructor
public class PathController {
    private final PathService pathService;

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(@AuthenticationPrincipal User loginUser, PathRequest pathRequest) {
        return ResponseEntity.ok(pathService.findPath(pathRequest, loginUser));
    }
}

package nextstep.subway.ui;

import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.auth.userdetails.UserDetails;
import nextstep.member.domain.LoginMember;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.PathSearchRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PathController {
    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(@AuthenticationPrincipal LoginMember user,
                                                 @RequestParam Long source,
                                                 @RequestParam Long target,
                                                 @RequestParam String type) {
        PathSearchRequest pathSearchRequest = new PathSearchRequest(source, target, type);
        return ResponseEntity.ok(pathService.findPath(pathSearchRequest, user.getAge()));
    }
}

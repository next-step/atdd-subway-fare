package nextstep.subway.ui;

import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.member.domain.LoginMember;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.PathSearchRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {
    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(@AuthenticationPrincipal LoginMember loginMember,
                                                 @RequestBody PathSearchRequest pathSearchRequest) {
        return ResponseEntity.ok(pathService.findPath(pathSearchRequest, loginMember.getAge()));
    }

//    @GetMapping("/paths")
//    public ResponseEntity<PathResponse> findPath(@RequestBody PathSearchRequest pathSearchRequest) {
//        return ResponseEntity.ok(pathService.findPath(pathSearchRequest));
//    }
}

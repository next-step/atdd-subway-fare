package nextstep.subway.ui;

import java.util.Optional;
import nextstep.auth.principal.AuthenticationPrincipal;
import nextstep.auth.principal.UserPrincipal;
import nextstep.member.application.MemberService;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.PathType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {
    private PathService pathService;
    private MemberService memberService;

    public PathController(PathService pathService, MemberService memberService) {
        this.pathService = pathService;
        this.memberService = memberService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(@RequestParam Long source,
        @RequestParam Long target, @RequestParam PathType type,
        @AuthenticationPrincipal(required = false) UserPrincipal userPrincipal) {

        Optional<Integer> userAge = memberService.findAgeByEmail(userPrincipal.getUsername());
        return ResponseEntity.ok(pathService.findPath(source, target, type, userAge));
    }
}

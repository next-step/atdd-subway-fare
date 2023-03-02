package nextstep.subway.ui;

import nextstep.member.application.MemberService;
import nextstep.member.domain.AuthenticationPrincipal;
import nextstep.member.domain.LoginMember;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.PathRequestType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {

    private final PathService pathService;
    private final MemberService memberService;

    public PathController(PathService pathService, MemberService memberService) {
        this.pathService = pathService;
        this.memberService = memberService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(
            @AuthenticationPrincipal(required = false) LoginMember loginMember,
            @RequestParam Long source,
            @RequestParam Long target,
            @RequestParam PathRequestType type) {
        PathResponse pathResponse = pathService.findPath(source, target, type, getAge(loginMember));

        return ResponseEntity.ok(pathResponse);
    }

    private int getAge(LoginMember loginMember) {
        if (loginMember.isEmpty()) {
            return 20;
        }
        return memberService.findMember(loginMember.getId()).getAge();
    }

}

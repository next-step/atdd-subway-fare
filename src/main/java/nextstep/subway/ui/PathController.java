package nextstep.subway.ui;

import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.member.domain.LoginMember;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.FindPathRequest;
import nextstep.subway.applicaion.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class PathController {
    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(@AuthenticationPrincipal(optional = true) LoginMember loginMember,
                                                 FindPathRequest request) {
        int age = getAge(loginMember);
        return ResponseEntity.ok(pathService.findPath(request, age));
    }

    private int getAge(LoginMember loginMember) {
        if (Objects.isNull(loginMember)) {
            return 0;
        }
        return loginMember.getAge();
    }

}

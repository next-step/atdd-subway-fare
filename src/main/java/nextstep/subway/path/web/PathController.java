package nextstep.subway.path.web;

import java.util.Optional;
import lombok.AllArgsConstructor;
import nextstep.auth.ui.AuthenticationPrincipal;
import nextstep.member.domain.LoginMember;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.application.dto.PathResponse;
import nextstep.subway.path.domain.PathType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paths")
@AllArgsConstructor
public class PathController {

    private final PathService pathService;

    @GetMapping
    public ResponseEntity<PathResponse> findPath(@RequestParam Long source,
        @RequestParam Long target, @RequestParam PathType type, @AuthenticationPrincipal(required = false)  LoginMember loginMember) {

        Optional<Integer> age = Optional.ofNullable(loginMember).map(LoginMember::getAge);
        return ResponseEntity.ok().body(pathService.findShortestPath(source, target, type, age));
    }
}

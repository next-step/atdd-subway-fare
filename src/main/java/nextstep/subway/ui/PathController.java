package nextstep.subway.ui;

import nextstep.member.domain.AuthenticationPrincipal;
import nextstep.member.domain.LoginMember;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathRequest;
import nextstep.subway.applicaion.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class PathController {
    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(@ModelAttribute PathRequest pathRequest,
                                                 @AuthenticationPrincipal LoginMember loginMember) {
        if (Objects.isNull(loginMember)) {
            return ResponseEntity.ok(pathService.findPath(pathRequest));
        }
        return ResponseEntity.ok(pathService.findPath(loginMember.getId(), pathRequest));
    }
}

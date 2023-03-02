package nextstep.subway.ui;

import nextstep.member.domain.LoginMember;
import nextstep.member.domain.LoginUser;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {
    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("paths")
    public ResponseEntity<PathResponse> findPath(@LoginUser final LoginMember loginUser, @RequestParam final Long source
            , @RequestParam final Long target, @RequestParam final String type) {
        return ResponseEntity.ok(pathService.findPath(loginUser, source, target, type));
    }
}

package nextstep.subway.ui;

import javax.validation.Valid;
import nextstep.auth.domain.LoginMember;
import nextstep.auth.ui.AuthenticationPrincipal;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathRequest;
import nextstep.subway.applicaion.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {
    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> getPath(@Valid PathRequest request, @AuthenticationPrincipal LoginMember loginMember) {
        return ResponseEntity.ok().body(pathService.getPath(request, loginMember));
    }
}

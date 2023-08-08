package nextstep.subway.ui;

import nextstep.auth.principal.AuthenticationPrincipal;
import nextstep.auth.userdetails.UserDetails;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathRequest;
import nextstep.subway.applicaion.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {
    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(@ModelAttribute PathRequest request
            , @AuthenticationPrincipal(required = false) UserDetails userDetails) {
        return ResponseEntity.ok(pathService.findPath(request, userDetails.getAge()));
    }
}

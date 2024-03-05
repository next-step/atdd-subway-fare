package nextstep.path.ui;

import nextstep.auth.ui.AuthenticationPrincipal;
import nextstep.auth.ui.UserPrincipal;
import nextstep.path.application.PathService;
import nextstep.path.application.dto.PathResponse;
import nextstep.path.application.dto.PathSearchRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(final PathSearchRequest searchRequest,
                                                 @AuthenticationPrincipal(required = false) final UserPrincipal userPrincipal
    ) {
        searchRequest.validate();
        searchRequest.setAge(userPrincipal.getAge());
        final PathResponse pathResponse = pathService.findShortestPath(searchRequest);
        return ResponseEntity.ok().body(pathResponse);
    }
}

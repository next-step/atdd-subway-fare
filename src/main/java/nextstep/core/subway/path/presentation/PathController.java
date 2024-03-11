package nextstep.core.subway.path.presentation;

import nextstep.core.auth.domain.LoginMember;
import nextstep.core.auth.presentation.AuthenticationPrincipal;
import nextstep.core.subway.path.application.PathService;
import nextstep.core.subway.path.application.dto.PathRequest;
import nextstep.core.subway.path.application.dto.PathFinderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {

    public final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathFinderResponse> findOptimalPath(@RequestParam("source") Long departureStationId,
                                                              @RequestParam("target") Long arrivalStationId,
                                                              @RequestParam("type") String pathFinderType,
                                                              @AuthenticationPrincipal(required = false) LoginMember loginMember) {
        PathRequest pathRequest = new PathRequest(departureStationId, arrivalStationId, pathFinderType);
        return ResponseEntity.ok(pathService.findOptimalPath(pathRequest, loginMember));
    }
}

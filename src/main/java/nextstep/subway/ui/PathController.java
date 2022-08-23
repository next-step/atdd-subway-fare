package nextstep.subway.ui;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathDto;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.PathType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import support.auth.authorization.AuthenticationPrincipal;
import support.auth.userdetails.UserDetails;

@RestController
public class PathController {
    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam Long source,
            @RequestParam Long target,
            @RequestParam("type") PathType type)
    {
        PathDto pathDto = PathDto.of(source, target, type);
        return ResponseEntity.ok(pathService.findPath((String) user.getUsername(), pathDto));
    }
}

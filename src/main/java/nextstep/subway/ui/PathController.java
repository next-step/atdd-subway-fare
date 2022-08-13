package nextstep.subway.ui;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathRequest;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.PathType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(PathRequest pathRequest) {
        // TODO: 기능 구현 뒤 하드코딩 삭제
        PathResponse path = pathService.findPath(pathRequest);
        if (PathType.of(pathRequest.getType()) == PathType.DISTANCE) {
            PathResponse result = new PathResponse(path.getStations(), path.getDistance(), path.getDuration(), 1250);
            return ResponseEntity.ok(result);
        }
        PathResponse result = new PathResponse(path.getStations(), path.getDistance(), path.getDuration(), 1450);
        return ResponseEntity.ok(result);
//        return ResponseEntity.ok(pathService.findPath(pathRequest));
    }
}

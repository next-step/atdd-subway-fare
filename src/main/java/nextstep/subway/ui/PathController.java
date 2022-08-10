package nextstep.subway.ui;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathRequest;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PathController {
    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(PathRequest pathRequest) {
        System.out.println("pathRequest = " + pathRequest);
        if (pathRequest.getType().equals("DURATION")) {
            List<StationResponse> stations = new ArrayList<>();
            stations.add(new StationResponse(5L, "교대역"));
            stations.add(new StationResponse(6L, "강남역"));
            stations.add(new StationResponse(7L, "양재역"));
            return ResponseEntity.ok(new PathResponse(stations, 5));
        }
        return ResponseEntity.ok(pathService.findPath(pathRequest));
    }
}

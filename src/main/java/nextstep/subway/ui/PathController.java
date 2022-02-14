package nextstep.subway.ui;

import java.util.Arrays;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.farepolicy.BasicFarePolicy;
import nextstep.subway.domain.farepolicy.DistanceFarePolicy;
import nextstep.subway.domain.farepolicy.DistanceFareRange;
import nextstep.subway.domain.farepolicy.FareCalculator;
import nextstep.subway.domain.farepolicy.FarePolicy;
import nextstep.subway.domain.map.OneFieldSubwayMapGraphFactory;
import nextstep.subway.domain.map.SubwayMapGraphFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("paths")
@RestController
public class PathController {
    private static final SubwayMapGraphFactory SUBWAY_MAP_GRAPH_FACTORY_FOR_DISTANCE = new OneFieldSubwayMapGraphFactory(
        section -> (double) section.getDistance()
    );
    private static final SubwayMapGraphFactory SUBWAY_MAP_GRAPH_FACTORY_FOR_DURATION = new OneFieldSubwayMapGraphFactory(
        section -> (double) section.getDuration()
    );
    private static final FarePolicy SUBWAY_FARE_POLICY = new FareCalculator(Arrays.asList(
        new BasicFarePolicy(), // 기본 요금 거리 정책
        new DistanceFarePolicy(new DistanceFareRange(10, 50), 5, 100), // 10km부터 50km 까지의 거리 정책
        new DistanceFarePolicy(new DistanceFareRange(50, Integer.MAX_VALUE), 8, 100) // 50km 초과의 거리 정책
    ));

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("distance")
    public ResponseEntity<PathResponse> findPathByDistance(@RequestParam Long source, @RequestParam Long target) {
        PathResponse pathResponse = pathService.findPath(
            source, target, SUBWAY_MAP_GRAPH_FACTORY_FOR_DISTANCE, SUBWAY_FARE_POLICY
        );
        return ResponseEntity.ok(pathResponse);
    }

    @GetMapping("duration")
    public ResponseEntity<PathResponse> findPathByDuration(@RequestParam Long source, @RequestParam Long target) {
        PathResponse pathResponse = pathService.findPath(
            source, target, SUBWAY_MAP_GRAPH_FACTORY_FOR_DURATION, SUBWAY_FARE_POLICY
        );
        return ResponseEntity.ok(pathResponse);
    }
}

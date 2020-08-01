package nextstep.subway.maps.map.ui;

import nextstep.subway.maps.map.application.FareMapService;
import nextstep.subway.maps.map.domain.PathType;
import nextstep.subway.maps.map.dto.FarePathResponse;
import nextstep.subway.maps.map.dto.MapResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MapController {
    private FareMapService mapService;

    public MapController(FareMapService mapService) {
        this.mapService = mapService;
    }

    @GetMapping("/paths")
    public ResponseEntity<FarePathResponse> findPath(@RequestParam Long source, @RequestParam Long target,
                                                 @RequestParam PathType type) {
        return ResponseEntity.ok(mapService.findPathWithFare(source, target, type));
    }

    @GetMapping("/maps")
    public ResponseEntity<MapResponse> findMap() {
        MapResponse response = mapService.findSubwayMap();
        return ResponseEntity.ok(response);
    }
}

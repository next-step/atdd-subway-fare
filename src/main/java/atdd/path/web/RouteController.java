package atdd.path.web;

import atdd.path.application.RouteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("routes")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping("distance")
    public ResponseEntity findShortestDistancePath(@RequestParam("startId") Long startId, @RequestParam("endId") Long endId) {
        return ResponseEntity.ok()
                .body(routeService.findShortestDistancePath(startId, endId));
    }

    @GetMapping("time")
    public ResponseEntity findShortestTimePath(@RequestParam("startId") Long startId, @RequestParam("endId") Long endId) {

        return ResponseEntity.ok()
                .body(routeService.findShortestTimePath(startId, endId));
    }
}

package subway.path.ui;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.path.application.PathService;
import subway.path.application.dto.PathRetrieveResponse;
import subway.path.domain.PathRetrieveType;

@RestController
@RequestMapping("/path")
@RequiredArgsConstructor
public class PathController {
    private final PathService pathService;

    @GetMapping
    public ResponseEntity<PathRetrieveResponse> getPath(@RequestParam Long source,
                                                        @RequestParam Long target,
                                                        @RequestParam(required = false, defaultValue = "DISTANCE") PathRetrieveType type) {
        PathRetrieveResponse response = pathService.getPath(source, target, type);
        return ResponseEntity.ok().body(response);
    }
}

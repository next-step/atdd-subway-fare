package nextstep.subway.ui;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.PathType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {
    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPathByDistance(@RequestParam Long source
        , @RequestParam Long target, @RequestParam PathType pathType) {
        //TODO if login 사용자라면 (SecurityContext 에 로그인정보가 있으면, 나이 확인)
        return ResponseEntity.ok(pathService.findPath(source, target, pathType));
    }
}

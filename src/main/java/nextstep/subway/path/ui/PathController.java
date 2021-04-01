package nextstep.subway.path.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.common.exception.ErrorResponse;
import nextstep.subway.line.domain.PathType;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.exception.DoesNotConnectedPathException;
import nextstep.subway.path.exception.SameStationPathSearchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PathResponse> findPath(@AuthenticationPrincipal LoginMember loginMember,
                                                 @RequestParam Long source, @RequestParam Long target, @RequestParam PathType type) {
        PathResponse pathResponse = pathService.findPath(loginMember.getAge(), source, target, type);
        return ResponseEntity.ok()
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .body(pathResponse);
    }

    @ExceptionHandler({DoesNotConnectedPathException.class, SameStationPathSearchException.class})
    public ResponseEntity<ErrorResponse> invalidPathExceptionHandler(RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.CONFLICT.value());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
}

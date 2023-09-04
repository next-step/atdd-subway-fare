package nextstep.subway.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import nextstep.subway.domain.Path;
import nextstep.subway.dto.PathResponse;
import nextstep.subway.service.PathService;
import nextstep.subway.validation.PathValidator;
import nextstep.subway.validation.impl.PathSourceTargetEqualsValidator;
import nextstep.subway.dto.PathRequest;

@RestController
@RequestMapping("/paths")
@RequiredArgsConstructor
public class PathController {

    private final PathService pathService;

    @GetMapping
    public ResponseEntity<PathResponse> findPath(@Valid PathRequest request) {
        PathValidator validator = new PathSourceTargetEqualsValidator(null); // validator chaining 가능
        validator.validate(request);

        Path path = pathService.findPath(request);
        return ResponseEntity.ok(PathResponse.from(path));
    }

}

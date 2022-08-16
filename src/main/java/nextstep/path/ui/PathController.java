package nextstep.path.ui;

import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.auth.userdetails.User;
import nextstep.path.applicaion.PathService;
import nextstep.path.applicaion.dto.PathRequest;
import nextstep.path.applicaion.dto.PathResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public PathResponse findPath(
            @AuthenticationPrincipal(required = false) User user,
            @ModelAttribute @Valid PathRequest pathRequest) {

        return pathService.findPath(user, pathRequest);
    }
}

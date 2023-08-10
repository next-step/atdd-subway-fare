package nextstep.subway.controller;

import lombok.RequiredArgsConstructor;
import nextstep.auth.principal.AuthenticationPrincipal;
import nextstep.auth.principal.UserPrincipal;
import nextstep.subway.domain.service.path.StationPathSearchRequestType;
import nextstep.subway.service.StationPathService;
import nextstep.subway.service.dto.StationPathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/paths")
public class StationPathController {
    private final StationPathService stationPathService;

    @GetMapping
    public ResponseEntity<StationPathResponse> getStationPath(
            @AuthenticationPrincipal(required = false) UserPrincipal userPrincipal,
            @RequestParam("source") Long startStationId,
            @RequestParam("target") Long destinationStationId,
            @RequestParam("type") StationPathSearchRequestType type) {

        final String email = Optional.ofNullable(userPrincipal)
                .map(UserPrincipal::getUsername)
                .orElse(null);

        return ResponseEntity.ok(stationPathService.searchStationPath(email, startStationId, destinationStationId, type));
    }
}

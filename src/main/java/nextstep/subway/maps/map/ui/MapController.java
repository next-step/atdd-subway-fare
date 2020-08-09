package nextstep.subway.maps.map.ui;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.auth.application.UserDetails;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.EmptyMember;
import nextstep.subway.maps.map.application.FareMapService;
import nextstep.subway.maps.map.domain.PathType;
import nextstep.subway.maps.map.dto.FarePathResponse;
import nextstep.subway.maps.map.dto.MapResponse;
import nextstep.subway.members.member.domain.LoginMember;

@RestController
public class MapController {
    private FareMapService mapService;

    public MapController(FareMapService mapService) {
        this.mapService = mapService;
    }

    @GetMapping("/paths")
    public ResponseEntity<FarePathResponse> findPath(@AuthenticationPrincipal UserDetails loginMember,
        @RequestParam Long source, @RequestParam Long target, @RequestParam PathType type,
        @DateTimeFormat(pattern = "yyyyMMddHHmm") @RequestParam(required = false, value = "time") LocalDateTime time) {
        if (loginMember instanceof EmptyMember) {
            return ResponseEntity.ok(mapService.findPathWithFare(source, target, type));
        }
        return ResponseEntity.ok(mapService.findPathWithFare((LoginMember)loginMember, source, target, type));
    }

    @GetMapping("/maps")
    public ResponseEntity<MapResponse> findMap() {
        MapResponse response = mapService.findSubwayMap();
        return ResponseEntity.ok(response);
    }
}

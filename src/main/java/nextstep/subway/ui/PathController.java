package nextstep.subway.ui;

import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import nextstep.auth.ui.AuthenticationPrincipal;
import nextstep.member.domain.LoginMember;
import nextstep.subway.applicaion.path.PathService;
import nextstep.subway.applicaion.dto.FindPathResponse;
import nextstep.subway.domain.PathSearchType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("paths")
public class PathController {

  private final PathService pathService;

  @GetMapping
  public ResponseEntity<FindPathResponse> findPath(
      @AuthenticationPrincipal LoginMember loginMember,
      @NotNull(message = "출발역 정보를 입력해주세요.") Long source,
      @NotNull(message = "도착역 정보를 입력해주세요.") Long target,
      PathSearchType type
  ) {
    return ResponseEntity.ok().body(pathService.findPath(source, target, type, loginMember.getEmail()));
  }

}

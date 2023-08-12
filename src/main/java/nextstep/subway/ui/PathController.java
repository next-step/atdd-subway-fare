package nextstep.subway.ui;

import java.util.Optional;
import nextstep.auth.principal.AuthenticationPrincipal;
import nextstep.auth.principal.UserPrincipal;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.PathWeight;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {

  public static final int NON_USER_AGE = 0;
  private PathService pathService;
  private MemberService memberService;

  public PathController(PathService pathService, MemberService memberService) {
    this.pathService = pathService;
    this.memberService = memberService;
  }
    // TODO
    // 구현된 부분 -> required로 비회원과 회원의 api가 명시적으로 구분됩니다.
    // 구현되지 못한 부분 -> Null Object(비회원) 일때 기본값을 넘겨주는 방법
    // 비회원 기능이 많다면 다른 방향으로 구현되야 할 것 같네요!

    // 이 부분은 어떻게 하면 더 깔끔하게 구현할 수 있을지 피드백이 필요한것같습니다.
    // Null Object의 default 행동을 어떻게 잡아야 age를 얻을 수 있을지 궁금하네요
    // 모든 OAUTH에 AGE가 들어가 있지 않으니 AGE를 Principal로 받는것도 이상해보입니다.

  @GetMapping("/paths")
  public ResponseEntity<PathResponse> findPath(@AuthenticationPrincipal(required = false) UserPrincipal userPrincipal,
      @RequestParam Long source, @RequestParam Long target, @RequestParam PathWeight type) {
    int age = NON_USER_AGE;

    if (userPrincipal.isLogin()) {
      age = memberService.findMemberByEmail(userPrincipal.getUsername()).getAge();
    }

    return ResponseEntity.ok().body(pathService.findPath(source, target, type, age));
  }
}

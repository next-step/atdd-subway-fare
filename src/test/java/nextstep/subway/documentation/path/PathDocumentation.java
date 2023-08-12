package nextstep.subway.documentation.path;

import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.study.AuthSteps.토큰_요청;
import static nextstep.subway.acceptance.PathSteps.두_역의_최단_거리_경로_유저로_조회를_요청;
import static nextstep.subway.documentation.path.PathDocumentationStep.pathErrorResponseFieldsSnippet;
import static nextstep.subway.documentation.path.PathDocumentationStep.pathRequestParameterSnippet;
import static nextstep.subway.documentation.path.PathDocumentationStep.pathResponseFieldsSnippet;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.documentation.Documentation;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("경로 조회 문서")
public class PathDocumentation extends Documentation {

  public static final String 이메일 = "admin@email.com";
  public static final String 비밀번호 = "password";
  public static final Integer 나이 = 20;
  public static final int FARE = 1350;
  public static final int DURATION = 12;
  public static final int DISTANCE = 10;

  public static final long 강남역아이디 = 1L;
  public static final long 역삼역아이디 = 2L;
  public static final String 강남역 = "강남역";
  public static final String 역삼역 = "역삼역";

  public static String 일반회원;
  @MockBean
  private PathService pathService;

  @BeforeEach
  void beforeEach() {
    회원_생성_요청(이메일, 비밀번호, 나이);
    일반회원 = 토큰_요청(이메일, 비밀번호).jsonPath().getString("accessToken");
  }

  @Test
  @DisplayName("일반회원으로 경로 조회를 요청한다.")
  void path() {
    PathResponse pathResponse = new PathResponse(
        Lists.newArrayList(
            new StationResponse(강남역아이디, 강남역),
            new StationResponse(역삼역아이디, 역삼역)
        ), DISTANCE, DURATION, FARE
    );
    when(pathService.findPath(anyLong(), anyLong(), any(), anyInt())).thenReturn(pathResponse);

    두_역의_최단_거리_경로_유저로_조회를_요청(강남역아이디, 역삼역아이디, "DURATION", 일반회원,
        getSpecification("path/findPath", pathRequestParameterSnippet(), pathResponseFieldsSnippet()));

  }

  @Test
  @DisplayName("이상한 타입으로 경로조회를 요청한다.")
  void errorWithWrongType() {

    두_역의_최단_거리_경로_유저로_조회를_요청(강남역아이디, 역삼역아이디, "ODD_TYPE", 일반회원,
        getSpecification("path/pathTypeError", pathRequestParameterSnippet(), pathErrorResponseFieldsSnippet()));

  }
}

package nextstep.acceptance.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.acceptance.steps.FavoriteSteps.*;
import static nextstep.acceptance.steps.MemberSteps.로그인_되어_있음;
import static nextstep.acceptance.steps.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "member@email.com";
    public static final String PASSWORD = "password";

    private Long 강남역;
    private Long 남부터미널역;
    private String 사용자;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_생성_요청(관리자, "강남역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청(관리자, "남부터미널역").jsonPath().getLong("id");

        사용자 = 로그인_되어_있음(EMAIL, PASSWORD);
    }

    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageFavorites() {
        // when
        var createResponse = 즐겨찾기_생성을_요청(사용자, 강남역, 남부터미널역);
        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        즐겨찾기_개수가_일치함(1);

        // when
        var duplicateCreateResponse = 즐겨찾기_생성을_요청(사용자, 강남역, 남부터미널역);
        // then
        assertThat(duplicateCreateResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        즐겨찾기_개수가_일치함(1);

        // when
        var deleteResponse = 즐겨찾기_삭제_요청(사용자, createResponse);
        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        즐겨찾기_개수가_일치함(0);
    }

    private void 즐겨찾기_개수가_일치함(int expected) {
        assertThat(즐겨찾기_목록_조회_요청(사용자).jsonPath().getList(".")).hasSize(expected);
    }
}
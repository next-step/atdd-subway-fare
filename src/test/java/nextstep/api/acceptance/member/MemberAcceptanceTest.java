package nextstep.api.acceptance.member;

import static nextstep.api.acceptance.auth.AuthSteps.일반_로그인_성공;
import static nextstep.api.acceptance.member.MemberSteps.내_정보_조회_성공;
import static nextstep.api.acceptance.member.MemberSteps.회원_삭제_성공;
import static nextstep.api.acceptance.member.MemberSteps.회원_생성_성공;
import static nextstep.api.acceptance.member.MemberSteps.회원_정보_수정_성공;
import static nextstep.api.acceptance.member.MemberSteps.회원_정보_조회_성공;
import static nextstep.api.acceptance.member.MemberSteps.회원_정보_조회됨;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.api.acceptance.AcceptanceTest;

class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
    }

    @DisplayName("회원가입을 한다.")
    @Test
    void createMember() {
        회원_생성_성공(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("회원 정보를 조회한다.")
    @Test
    void getMember() {
        // given
        final var memberId = 회원_생성_성공(EMAIL, PASSWORD, AGE).getId();

        // when
        final var response = 회원_정보_조회_성공(memberId);

        // then
        회원_정보_조회됨(response, EMAIL, AGE);
    }

    @DisplayName("회원 정보를 수정한다.")
    @Test
    void updateMember() {
        // given
        final var memberId = 회원_생성_성공(EMAIL, PASSWORD, AGE).getId();

        // when & then
        회원_정보_수정_성공(memberId, "new" + EMAIL, "new" + PASSWORD, AGE);
    }

    @DisplayName("회원 정보를 삭제한다.")
    @Test
    void deleteMember() {
        // given
        final var memberId = 회원_생성_성공(EMAIL, PASSWORD, AGE).getId();

        // when & then
        회원_삭제_성공(memberId);
    }

    @DisplayName("내 정보를 조회한다.")
    @Test
    void getMyInfo() {
        // given
        회원_생성_성공(EMAIL, PASSWORD, AGE);
        final var token = 일반_로그인_성공(EMAIL, PASSWORD).getAccessToken();

        // when
        final var response = 내_정보_조회_성공(token);

        // then
        회원_정보_조회됨(response, EMAIL, AGE);
    }
}

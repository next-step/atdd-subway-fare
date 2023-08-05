package subway.acceptance.member;

import subway.acceptance.auth.AuthFixture;

public class MemberFixture {

    public static String 어린이_회원_생성_로그인_후_토큰(){
        final String EMAIL = "child@email.com";
        final String PASSWORD = "password";
        final int AGE = 7;
        MemberSteps.회원_생성_요청(EMAIL, PASSWORD, AGE);
        return AuthFixture.로그인_후_토큰_추출(EMAIL, PASSWORD);

    }

    public static String 청소년_회원_생성_로그인_후_토큰(){
        final String EMAIL = "teenager@email.com";
        final String PASSWORD = "password";
        final int AGE = 17;
        MemberSteps.회원_생성_요청(EMAIL, PASSWORD, AGE);
        return AuthFixture.로그인_후_토큰_추출(EMAIL, PASSWORD);
    }

    public static String 일반_회원_생성_로그인_후_토큰(){
        final String EMAIL = "teenager@email.com";
        final String PASSWORD = "password";
        final int AGE = 23;
        MemberSteps.회원_생성_요청(EMAIL, PASSWORD, AGE);
        return AuthFixture.로그인_후_토큰_추출(EMAIL, PASSWORD);
    }


}

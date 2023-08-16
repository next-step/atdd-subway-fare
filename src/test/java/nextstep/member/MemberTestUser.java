package nextstep.member;

import nextstep.auth.principal.AnonymousPrincipal;
import nextstep.auth.principal.UserPrincipal;

import static nextstep.member.MemberTestField.*;

public class MemberTestUser {
    public static UserPrincipal 어린이 = new UserPrincipal(EMAIL, ROLE, CHILDREN_AGE);
    public static UserPrincipal 청소년 = new UserPrincipal(EMAIL, ROLE, TEENAGER_AGE);
    public static UserPrincipal 성인 = new UserPrincipal(EMAIL, ROLE, ADULT_AGE);
    public static AnonymousPrincipal 비회원 = new AnonymousPrincipal();
}

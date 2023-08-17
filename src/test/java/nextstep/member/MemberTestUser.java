package nextstep.member;

import nextstep.auth.principal.AnonymousPrincipal;
import nextstep.auth.principal.UserPrincipal;
import nextstep.member.domain.Member;

import static nextstep.member.MemberTestField.*;

public class MemberTestUser {
    public static UserPrincipal 어린이인증 = new UserPrincipal(CHILDREN_EMAIL, ROLE);
    public static UserPrincipal 청소년인증 = new UserPrincipal(TEENAGER_EMAIL, ROLE);
    public static UserPrincipal 성인인증 = new UserPrincipal(ADULT_EMAIL, ROLE);
    public static AnonymousPrincipal 비회원인증 = new AnonymousPrincipal();

    public static Member 어린이 = new Member(CHILDREN_EMAIL, PASSWORD, CHILDREN_AGE, ROLE);
    public static Member 청소년 = new Member(TEENAGER_EMAIL, PASSWORD, TEENAGER_AGE, ROLE);
    public static Member 성인 = new Member(ADULT_EMAIL, PASSWORD, ADULT_AGE, ROLE);
    public static Member 비회원 = null;
}

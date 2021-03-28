package nextstep.subway.member.application;

import nextstep.subway.auth.application.AnonymousDetail;
import nextstep.subway.auth.domain.UserType;
import nextstep.subway.member.domain.LoginMember;

public class AnonymousMemberDetail implements AnonymousDetail {
    public static final String EMAIL = "anonymous@email.com";
    public static final String PASSWORD = "anonymouspassword";
    public static final int AGE = 0;

    public static final LoginMember anonymous = new LoginMember(null, EMAIL, PASSWORD, AGE, UserType.ANONYMOUS.name());

    @Override
    public Object anonymous() {
        return anonymous;
    }
}

package nextstep.subway.member.domain;

import nextstep.subway.auth.application.AnonymousDetails;
import nextstep.subway.auth.application.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class Anonymous implements AnonymousDetails {
    private static final String EMAIL = "anonymous@mail.com";
    private static final String PASSWORD = "anonymous";
    private static final int AGE = 0;
    private static LoginMember notLogin;

    @Override
    public UserDetails getAnonymous() {
        if (Objects.isNull(notLogin)) {
            notLogin = new LoginMember(0L, EMAIL, PASSWORD, AGE);
        }
        return notLogin;
    }
}

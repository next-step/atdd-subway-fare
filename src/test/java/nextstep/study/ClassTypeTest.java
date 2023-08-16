package nextstep.study;

import nextstep.auth.principal.AnonymousPrincipal;
import nextstep.auth.principal.UserPrincipal;
import org.junit.jupiter.api.Test;

import static nextstep.member.MemberTestField.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ClassTypeTest {
    @Test
    void 클래스연산자테스트() {
        UserPrincipal userPrincipal = new UserPrincipal(EMAIL, ROLE, AGE);
        AnonymousPrincipal anonymousPrincipal = new AnonymousPrincipal();
        assertThat(userPrincipal instanceof UserPrincipal).isTrue();
        assertThat(userPrincipal instanceof AnonymousPrincipal).isFalse();
        assertThat(userPrincipal.getClass().isAssignableFrom(UserPrincipal.class)).isTrue();
        assertThat(userPrincipal.getClass().isAssignableFrom(AnonymousPrincipal.class)).isTrue();
        assertThat(anonymousPrincipal instanceof UserPrincipal).isTrue();
        assertThat(anonymousPrincipal instanceof AnonymousPrincipal).isTrue();
        assertThat(anonymousPrincipal.getClass().isAssignableFrom(UserPrincipal.class)).isFalse();
        assertThat(anonymousPrincipal.getClass().isAssignableFrom(AnonymousPrincipal.class)).isTrue();
    }
}

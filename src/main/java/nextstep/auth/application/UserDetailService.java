package nextstep.auth.application;

import nextstep.auth.domain.LoginMember;
import nextstep.auth.domain.UserDetails;

import java.util.function.Supplier;

public interface UserDetailService {
    UserDetails findOrElseDefault(String email, Supplier<UserDetails> defaultSupplier);

    UserDetails findByEmail(String email);

    int getUserAge(LoginMember loginMember);
}

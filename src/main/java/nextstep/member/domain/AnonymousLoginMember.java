package nextstep.member.domain;

import java.util.Optional;

public class AnonymousLoginMember extends LoginMember {
    public AnonymousLoginMember() {
        super("AnonymousLoginMember");
    }

    @Override
    public String getEmail() {
        return super.getEmail();
    }

    public static Optional<AnonymousLoginMember> createOptional() {
        return Optional.of(new AnonymousLoginMember());
    }
}


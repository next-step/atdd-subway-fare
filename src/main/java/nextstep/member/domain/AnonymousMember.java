package nextstep.member.domain;

import java.util.List;

public class AnonymousMember implements IdentificationMember {

    public AnonymousMember() {

    }

    @Override
    public Long getId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> getRoles() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isAnonymousMember() {
        return true;
    }
}

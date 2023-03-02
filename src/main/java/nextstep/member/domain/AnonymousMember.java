package nextstep.member.domain;

import java.util.List;

public class AnonymousMember extends LoginMember {

    public AnonymousMember() {
        super(null, null);
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

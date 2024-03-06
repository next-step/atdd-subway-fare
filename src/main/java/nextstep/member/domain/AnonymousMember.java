package nextstep.member.domain;

public class AnonymousMember extends Member {

    public AnonymousMember() {
    }

    @Override
    public boolean isAnonymous() {
        return true;
    }
}

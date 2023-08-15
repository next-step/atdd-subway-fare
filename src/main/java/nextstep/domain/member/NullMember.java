package nextstep.domain.member;

public class NullMember extends AbstractMember{
    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public Integer getAge() {
        return null;
    }
}

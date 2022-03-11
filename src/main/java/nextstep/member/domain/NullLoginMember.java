package nextstep.member.domain;

public class NullLoginMember extends LoginMember{
    private static final NullLoginMember INSTANCE = new NullLoginMember();

    private NullLoginMember() {
        super(null, null, null, 0);
    }

    public static NullLoginMember getInstance() {
        return INSTANCE;
    }

    @Override
    public Integer getAge() {
        return 0;
    }
}

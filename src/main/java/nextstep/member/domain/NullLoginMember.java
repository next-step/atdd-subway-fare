package nextstep.member.domain;

public class NullLoginMember extends LoginMember{
    private static final NullLoginMember instance = new NullLoginMember();

    private NullLoginMember() {
        this(null, null, null, 0);
    }

    private NullLoginMember(Long id, String email, String password, Integer age) {
        super(id, email, password, age);
    }

    public static NullLoginMember getInstance() {
        return instance;
    }

    @Override
    public Integer getAge() {
        return 0;
    }
}

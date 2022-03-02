package nextstep.member.domain;

public class NonLoginMember extends LoginMember {

    private static final int DEFAULT_AGE_OF_ADULT = 20;

    private static class LazyHolder {
        private static final NonLoginMember instance = new NonLoginMember();
    }

    public static LoginMember getInstance() {
        return LazyHolder.instance;
    }

    private NonLoginMember() {
        super();
    }

    @Override
    public Integer getAge() {
        return DEFAULT_AGE_OF_ADULT;
    }
}

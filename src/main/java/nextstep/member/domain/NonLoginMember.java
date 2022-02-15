package nextstep.member.domain;

public class NonLoginMember extends LoginMember {

    private static final String NON_LOGIN = "nonLogin";
    private static final int NON_LOGIN_AGE = 20;

    private NonLoginMember(Long id, String email, String password, Integer age) {
        super(id, email, password, age);
    }

    private static class NonLoginMemberHolder {
        private static final NonLoginMember INSTANCE =
                new NonLoginMember(null, NON_LOGIN, NON_LOGIN, NON_LOGIN_AGE);
    }

    public static NonLoginMember getInstance() {
        return NonLoginMemberHolder.INSTANCE;
    }
}

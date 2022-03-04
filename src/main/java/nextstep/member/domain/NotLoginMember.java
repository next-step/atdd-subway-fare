package nextstep.member.domain;

public class NotLoginMember extends LoginMember {

    private NotLoginMember(Long id, String email, String password, Integer age) {
        super(id, email, password, age);
    }

    private static class NotLoginMemberHolder {
        private static final NotLoginMember INSTANCE =
                new NotLoginMember(0L, "", "", 0);
    }

    public static NotLoginMember getInstance() {
        return NotLoginMemberHolder.INSTANCE;
    }
}

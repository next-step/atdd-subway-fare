package nextstep.member.domain;

public class NonLoginMember extends LoginMember {

    private final int nonLoginMemberAge = 20;

    public NonLoginMember() {
        super();
    }

    @Override
    public Integer getAge() {
        return nonLoginMemberAge;
    }

    @Override
    public boolean checkCredentials(String credentials) {
        return getPassword().equals(credentials);
    }
}

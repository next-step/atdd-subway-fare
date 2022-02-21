package nextstep.member.domain;

public class NonLoginMember extends LoginMember {

    private final int nonLoginMemberAge = 20;

    public NonLoginMember() {
        super();
    }

    private NonLoginMember(Long id, String email, String password, Integer age) {
        super(id, email, password, age);
    }

    @Override
    public Long getId() {
        return Long.MIN_VALUE;
    }

    @Override
    public String getEmail() {
        return "nonLogin@email.com";
    }

    @Override
    public String getPassword() {
        return "nonLoginPassword";
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

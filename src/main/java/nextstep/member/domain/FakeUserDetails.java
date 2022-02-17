package nextstep.member.domain;

public class FakeUserDetails extends LoginMember {

    public FakeUserDetails() {
        super();
    }

    private FakeUserDetails(Long id, String email, String password, Integer age) {
        super(id, email, password, age);
    }

    @Override
    public Long getId() {
        return Long.MIN_VALUE;
    }

    @Override
    public String getEmail() {
        return "fake@email.com";
    }

    @Override
    public String getPassword() {
        return "fakePassword";
    }

    @Override
    public Integer getAge() {
        return 20;
    }

    @Override
    public boolean checkCredentials(String credentials) {
        return getPassword().equals(credentials);
    }
}

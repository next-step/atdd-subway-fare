package nextstep.member.domain;

import nextstep.auth.userdetails.UserDetails;

public class AnonymousMember extends LoginMember{
    public AnonymousMember(Long id, String email, String password, Integer age) {
        super(id, email, password, age);
    }

    @Override
    public boolean checkPassword(String password) {
        return super.checkPassword(password);
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public String getEmail() {
        return super.getEmail();
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public Integer getAge() {
        return super.getAge();
    }

    @Override
    public Object getPrincipal() {
        return super.getPrincipal();
    }

    @Override
    public Object getCredentials() {
        return super.getCredentials();
    }

    @Override
    public boolean checkCredentials(Object credentials) {
        return super.checkCredentials(credentials);
    }
}

package nextstep.subway.member.domain;


import nextstep.subway.auth.application.UserDetails;
import nextstep.subway.auth.domain.UserType;

public class LoginMember implements UserDetails {
    private Long id;
    private String email;
    private String password;
    private Integer age;
    private UserType userType;

    public static LoginMember of(Member member) {
        return new LoginMember(member.getId(), member.getEmail(), member.getPassword(), member.getAge());
    }

    public LoginMember(Long id, String email, String password, Integer age) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.age = age;
        this.userType = UserType.USER;
    }

    public LoginMember(Long id, String email, String password, Integer age, String userType) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.age = age;
        this.userType = UserType.get(userType);
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    public String getPassword() {
        return password;
    }

    public UserType getUserType() {
        return userType;
    }

    @Override
    public Object getPrincipal() {
        return email;
    }

    @Override
    public Object getCredentials() {
        return password;
    }

    @Override
    public boolean checkCredentials(Object credentials) {
        return this.password.equals(credentials.toString());
    }
}

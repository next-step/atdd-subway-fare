package support.auth.userdetails;

import nextstep.member.domain.RoleType;

import java.util.List;

public class User implements UserDetails {
    private static final int DEFAULT_AGE = 20;
    private String username;
    private String password;
    private int age;
    private List<String> authorities;

    public User(String username, String password, int age, List<String> authorities) {
        this.username = username;
        this.password = password;
        this.age = age;
        this.authorities = authorities;
    }

    public static User anonymous() {
        return new User("Anonymous", "Anonymous", DEFAULT_AGE, List.of(RoleType.ROLE_ANONYMOUS.name()));
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public int getAge() {
        return age;
    }

    @Override
    public List<String> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean checkCredentials(Object credentials) {
        return password.equals(credentials.toString());
    }
}

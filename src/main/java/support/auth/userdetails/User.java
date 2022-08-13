package support.auth.userdetails;

import java.util.List;

public class User implements UserDetails {

    public static final int DEFAULT_ADULT = 20;

    private String username;
    private String password;
    private List<String> authorities;
    private int age;

    public User(String username, String password, List<String> authorities, int age) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.age = age;
    }

    public static User nonLoginUser() {
        return new User(null, null, null, DEFAULT_ADULT);
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public List<String> getAuthorities() {
        return authorities;
    }

    @Override
    public Integer getAge() {
        return age;
    }

    @Override
    public boolean checkCredentials(Object credentials) {
        return password.equals(credentials.toString());
    }
}

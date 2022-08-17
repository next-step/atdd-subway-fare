package support.auth.userdetails;

import java.util.Collections;
import java.util.List;

public class User implements UserDetails {
    private String username;
    private String password;
    private Integer age;
    private List<String> authorities;

    public User(String username, String password, Integer age, List<String> authorities) {
        this.username = username;
        this.password = password;
        this.age = age;
        this.authorities = authorities;
    }

    public static User guest() {
        return new User(null, null, null, Collections.emptyList());
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
    public boolean checkCredentials(Object credentials) {
        return password.equals(credentials.toString());
    }

    @Override
    public Integer getAge() {
        return age;
    }
}

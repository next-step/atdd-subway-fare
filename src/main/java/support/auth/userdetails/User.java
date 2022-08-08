package support.auth.userdetails;

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

    public Integer getAge() {
        return age;
    }

    @Override
    public boolean checkCredentials(Object credentials) {
        return password.equals(credentials.toString());
    }
}

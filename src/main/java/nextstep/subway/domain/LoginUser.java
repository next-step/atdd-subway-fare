package nextstep.subway.domain;

import lombok.Getter;
import support.auth.userdetails.UserDetails;

import java.util.List;

@Getter
public class LoginUser implements UserDetails {
    private String username;
    private String password;
    private int age;
    private List<String> authorities;

    public LoginUser(String username, String password, int age, List<String> authorities) {
        this.username = username;
        this.password = password;
        this.age = age;
        this.authorities = authorities;
    }

    @Override
    public boolean checkCredentials(Object credentials) {
        return password.equals(credentials.toString());
    }

}

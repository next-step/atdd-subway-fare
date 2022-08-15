package support.auth.context;

import java.io.Serializable;
import java.util.List;

public class Authentication implements Serializable {
    private Object principal;
    private int age;
    private List<String> authorities;

    public Authentication(Object principal, int age, List<String> authorities) {
        this.principal = principal;
        this.age = age;
        this.authorities = authorities;
    }

    public Object getPrincipal() {
        return principal;
    }

    public int getAge() {
        return age;
    }

    public List<String> getAuthorities() {
        return authorities;
    }
}

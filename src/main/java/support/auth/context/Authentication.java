package support.auth.context;

import java.io.Serializable;
import java.util.List;

public class Authentication implements Serializable {
    private Object principal;
    private List<String> authorities;
    private int age;

    public Authentication(Object principal, List<String> authorities, int age) {
        this.principal = principal;
        this.authorities = authorities;
        this.age = age;
    }

    public Object getPrincipal() {
        return principal;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public int getAge() {
        return age;
    }

}

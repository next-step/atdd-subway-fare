package support.auth.context;

import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class Authentication implements Serializable {
    private Object principal;
    private int age;
    private List<String> authorities;

    public Authentication(Object principal, int age, List<String> authorities) {
        this.principal = principal;
        this.age = age;
        this.authorities = authorities;
    }
}
